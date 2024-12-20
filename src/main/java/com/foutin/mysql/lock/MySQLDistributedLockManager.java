package com.foutin.mysql.lock;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author XingKong
 * @Date 2023/6/7 14:32
 */
@Component
@Slf4j
public class MySQLDistributedLockManager implements IDistributedLockManager {
    /**
     * 定时续期时设定的过期时间（单位：毫秒）
     */
    private static final int DEFAULT_RENEWAL_EXPIRE_INTERVAL = 90000;
    /**
     * 锁续期间隔时间（单位：毫秒）
     */
    private static final int RENEWAL_LOCK_INTERVAL = 60000;

    private static final String SCHEDULER_LOCK_KEY = "DistributedLock_{lockName}_{lockValue}";

    /**
     * 本地锁
     */
    private final Map<String, ReentrantLock> localLockMap = Maps.newConcurrentMap();
    /**
     * 远程锁信息记录
     */
    private final ThreadLocal<Map<String, RemoteLockInfo>> remoteLockInfoThreadLocal =
            ThreadLocal.withInitial(Maps::newHashMap);

    /**
     * 存活锁集合
     */
    private final Set<String> lockValues = Sets.newConcurrentHashSet();

    // @Resource
    // private GlobalLockMapper globalLockMapper;

    @Resource
    private GlobalLockAssistant globalLockAssistant;

    @Resource
    private CancelledWheelScheduler wheelScheduler;

    @Override
    public void lock(DistributedLockModuleEnum lockModule) {
        this.lock(lockModule.getLockName(), lockModule.getPollGap(), lockModule.getExpireInterval(),
                lockModule.getWaitTime());
    }

    @Override
    public void lock(DistributedLockModuleEnum lockModule, String lockKey) {
        this.lock(lockModule.getLockName(lockKey), lockModule.getPollGap(), lockModule.getExpireInterval(),
                lockModule.getWaitTime());
    }

    @Override
    public void lock(String lockName) {
        this.lock(lockName, DistributedLockConstants.DEFAULT_POLL_GAP, DistributedLockConstants.DEFAULT_EXPIRE_INTERVAL,
                DistributedLockConstants.DEFAULT_WAIT_TIME);
    }

    @Override
    public void lock(String lockName, long timeout, TimeUnit unit) {
        this.lock(lockName, DistributedLockConstants.DEFAULT_POLL_GAP, DistributedLockConstants.DEFAULT_EXPIRE_INTERVAL,
                unit.toMillis(timeout));
    }

    /**
     * 加锁（带超时时间需处理 DCTimeoutException 异常）
     *
     * @param lockName       锁名称
     * @param pollGap        轮询抢占锁间隔时间（单位：毫秒）
     * @param expireInterval 锁过期时间，-1 表示会定时续期（单位：毫秒）
     * @param waitTime       锁等待时间，-1 表示永久等待（单位：毫秒）
     */
    private void lock(String lockName, int pollGap, int expireInterval, long waitTime) {
        long triggerTime = System.currentTimeMillis();
        if (pollGap < 0) {
            pollGap = DistributedLockConstants.DEFAULT_POLL_GAP;
        }
        boolean needRenewalLock = false;
        if (expireInterval < 0) {
            needRenewalLock = true;
            expireInterval = DEFAULT_RENEWAL_EXPIRE_INTERVAL;
        }

        // 本地锁控制
        this.doLocalLock(lockName, waitTime);

        // 锁重入检查
        RemoteLockInfo remoteLockInfo = remoteLockInfoThreadLocal.get().computeIfAbsent(lockName,
                currLockName -> new RemoteLockInfo());
        if (remoteLockInfo.getReentrantCount() > 0) {
            remoteLockInfo.setReentrantCount(remoteLockInfo.getReentrantCount() + 1);
            return;
        }

        // 分布式锁控制
        String lockValue = UUID.randomUUID().toString().replace("-", "");
        while (true) {
            boolean lockSuccess;
            long lockId;
            try {
                Pair<Boolean, Long> pair = this.doRemoteLock(lockName, lockValue, expireInterval, false);
                lockSuccess = pair.getLeft();
                lockId = pair.getRight();
            } catch (RuntimeException e) {
                this.handleRemoteLockFail(lockName);
                throw e;
            }
            if (lockSuccess) {
                remoteLockInfo.setReentrantCount(1);
                remoteLockInfo.setLockId(lockId);
                remoteLockInfo.setLockValue(lockValue);
                lockValues.add(lockValue);
                if (needRenewalLock) {
                    // 定时锁续期
                    this.scheduleRenewalLock(lockName, lockId, lockValue);
                }
                break;
            } else {
                if (waitTime >= 0 && System.currentTimeMillis() - triggerTime >= waitTime) {
                    this.handleRemoteLockFail(lockName);
                    throw new RuntimeException(String.format("fetch lock timeout. lockName: %s; waitTime: %d",
                            lockName, waitTime));
                } else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(pollGap);
                    } catch (InterruptedException e) {
                        this.handleRemoteLockFail(lockName);
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * 本地加锁
     */
    private void doLocalLock(String lockName, long waitTime) {
        ReentrantLock localLock = localLockMap.computeIfAbsent(lockName, currLockName -> new ReentrantLock());
        if (waitTime < 0) {
            localLock.lock();
            remoteLockInfoThreadLocal.get().computeIfAbsent(lockName, currLockName -> new RemoteLockInfo())
                    .setAssociatedLocalLock(localLock);
        } else {
            try {
                boolean success = localLock.tryLock(waitTime, TimeUnit.MILLISECONDS);
                if (success) {
                    remoteLockInfoThreadLocal.get().computeIfAbsent(lockName, currLockName -> new RemoteLockInfo())
                            .setAssociatedLocalLock(localLock);
                } else {
                    throw new RuntimeException(String.format("fetch lock timeout. lockName: %s; waitTime: %d",
                            lockName, waitTime));
                }
            } catch (InterruptedException e) {
                if (localLock.getHoldCount() <= 0 && localLock.getQueueLength() <= 0) {
                    localLockMap.remove(lockName, localLock);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 远程加锁
     */
    private Pair<Boolean, Long> doRemoteLock(String lockName, String lockValue, int expireInterval, boolean tried) {
        // GlobalLockEntity globalLockEntity =
        //         globalLockMapper.selectOne(new LambdaQueryWrapper<GlobalLockEntity>().eq
        //         (GlobalLockEntity::getLockName, lockName));
        // if (globalLockEntity == null) {
        //     try {
        //         globalLockEntity = globalLockAssistant.insertLock(lockName, lockValue, expireInterval);
        //         return Pair.of(true, globalLockEntity.getId());
        //     } catch (DuplicateKeyException e) {
        //         globalLockEntity = globalLockMapper.selectOne(new LambdaQueryWrapper<GlobalLockEntity>()
        //                 .eq(GlobalLockEntity::getLockName, lockName));
        //         if (globalLockEntity == null) {
        //             return Pair.of(false, 0L);
        //         }
        //     }
        // }
        // if (globalLockEntity.getLockFlag() && globalLockEntity.getExpireTime().after(new Date())) {
        //     return Pair.of(false, 0L);
        // }
        // boolean success = globalLockAssistant.addLock(globalLockEntity.getId(), lockValue, expireInterval,
        //         globalLockEntity.getVersion());
        // long lockId = 0;
        // if (success) {
        //     lockId = globalLockEntity.getId();
        // } else if (tried && !globalLockMapper.exists(new LambdaQueryWrapper<GlobalLockEntity>()
        //         .eq(GlobalLockEntity::getLockName, lockName))) {
        //     try {
        //         globalLockEntity = globalLockAssistant.insertLock(lockName, lockValue, expireInterval);
        //         success = true;
        //         lockId = globalLockEntity.getId();
        //     } catch (DuplicateKeyException e) {
        //         log.warn(e.getMessage(), e);
        //     }
        // }
        // return Pair.of(success, lockId);
        return null;
    }

    /**
     * 处理远程加锁失败
     */
    private void handleRemoteLockFail(String lockName) {
        ReentrantLock localLock = null;
        try {
            Map<String, RemoteLockInfo> remoteLockInfoMap = remoteLockInfoThreadLocal.get();
            RemoteLockInfo remoteLockInfo = remoteLockInfoMap.remove(lockName);
            if (remoteLockInfoMap.size() == 0) {
                remoteLockInfoThreadLocal.remove();
            }
            if (remoteLockInfo != null) {
                localLock = remoteLockInfo.getAssociatedLocalLock();
            }
        } finally {
            // 本地释放锁
            if (localLock != null) {
                this.doLocalUnlock(lockName, localLock);
            }
        }
    }

    @Override
    public boolean tryLock(DistributedLockModuleEnum lockModule) {
        return this.tryLock(lockModule.getLockName(), lockModule.getExpireInterval());
    }

    @Override
    public boolean tryLock(DistributedLockModuleEnum lockModule, String lockKey) {
        return this.tryLock(lockModule.getLockName(lockKey), lockModule.getExpireInterval());
    }

    @Override
    public boolean tryLock(String lockName) {
        return this.tryLock(lockName, DistributedLockConstants.DEFAULT_EXPIRE_INTERVAL);
    }

    /**
     * 尝试加锁
     *
     * @param lockName       锁名称
     * @param expireInterval 锁过期时间，-1 表示会定时续期（单位：毫秒）
     */
    private boolean tryLock(String lockName, int expireInterval) {
        boolean needRenewalLock = false;
        if (expireInterval < 0) {
            needRenewalLock = true;
            expireInterval = DEFAULT_RENEWAL_EXPIRE_INTERVAL;
        }

        // 本地锁控制
        boolean lockSuccess = this.doLocalTryLock(lockName);
        if (!lockSuccess) {
            return false;
        }

        // 锁重入检查
        RemoteLockInfo remoteLockInfo = remoteLockInfoThreadLocal.get().computeIfAbsent(lockName,
                currLockName -> new RemoteLockInfo());
        if (remoteLockInfo.getReentrantCount() > 0) {
            remoteLockInfo.setReentrantCount(remoteLockInfo.getReentrantCount() + 1);
            return true;
        }

        // 分布式锁控制
        String lockValue = UUID.randomUUID().toString().replace("-", "");
        long lockId;
        try {
            Pair<Boolean, Long> pair = this.doRemoteLock(lockName, lockValue, expireInterval, true);
            lockSuccess = pair.getLeft();
            lockId = pair.getRight();
        } catch (RuntimeException e) {
            this.handleRemoteLockFail(lockName);
            throw e;
        }
        if (lockSuccess) {
            remoteLockInfo.setReentrantCount(1);
            remoteLockInfo.setLockId(lockId);
            remoteLockInfo.setLockValue(lockValue);
            lockValues.add(lockValue);
            if (needRenewalLock) {
                this.scheduleRenewalLock(lockName, lockId, lockValue);  // 定时锁续期
            }
        } else {
            this.handleRemoteLockFail(lockName);
        }
        return lockSuccess;
    }

    /**
     * 本地尝试加锁
     */
    private boolean doLocalTryLock(String lockName) {
        ReentrantLock localLock = localLockMap.computeIfAbsent(lockName, currLockName -> new ReentrantLock());
        boolean lockSuccess = localLock.tryLock();
        if (lockSuccess) {
            remoteLockInfoThreadLocal.get().computeIfAbsent(lockName, currLockName -> new RemoteLockInfo())
                    .setAssociatedLocalLock(localLock);
        }
        return lockSuccess;
    }

    @Override
    public boolean unlock(DistributedLockModuleEnum lockModule) {
        return this.unlock(lockModule.getLockName(), lockModule.getEvictInterval());
    }

    @Override
    public boolean unlock(DistributedLockModuleEnum lockModule, String lockKey) {
        return this.unlock(lockModule.getLockName(lockKey), lockModule.getEvictInterval());
    }

    @Override
    public boolean unlock(String lockName) {
        return this.unlock(lockName, DistributedLockConstants.DEFAULT_EVICT_INTERVAL);
    }

    /**
     * 释放锁
     *
     * @param lockName      锁名称
     * @param evictInterval 锁清除时间，-1 表示永不清除（单位：毫秒）
     */
    private boolean unlock(String lockName, long evictInterval) {
        ReentrantLock localLock = null;
        try {
            if (evictInterval < 0) {
                evictInterval = Integer.MAX_VALUE * 2L;
            }
            // 锁重入释放
            Map<String, RemoteLockInfo> remoteLockInfoMap = remoteLockInfoThreadLocal.get();
            RemoteLockInfo remoteLockInfo = remoteLockInfoMap.get(lockName);
            if (remoteLockInfo != null) {
                localLock = remoteLockInfo.getAssociatedLocalLock();
                if (remoteLockInfo.getReentrantCount() > 0) {
                    remoteLockInfo.setReentrantCount(remoteLockInfo.getReentrantCount() - 1);
                }
            }
            if (remoteLockInfo != null && remoteLockInfo.getReentrantCount() > 0) {
                return true;
            }

            // 清理本地记录
            remoteLockInfoMap.remove(lockName);
            if (remoteLockInfoMap.size() == 0) {
                remoteLockInfoThreadLocal.remove();
            }

            // 释放分布式锁
            if (remoteLockInfo == null || StringUtils.isBlank(remoteLockInfo.getLockValue())) {
                return false;
            }
            lockValues.remove(remoteLockInfo.getLockValue());
            // 取消定时锁续期
            this.cancelScheduleRenewalLock(lockName, remoteLockInfo.getLockValue());
            return this.doRemoteUnlock(remoteLockInfo.getLockId(), remoteLockInfo.getLockValue(), evictInterval);
        } finally {
            // 本地释放锁
            if (localLock != null) {
                this.doLocalUnlock(lockName, localLock);
            }
        }
    }

    /**
     * 本地释放锁
     */
    private void doLocalUnlock(String lockName, ReentrantLock localLock) {
        localLock.unlock();
        if (localLock.getHoldCount() <= 0 && localLock.getQueueLength() <= 0) {
            localLockMap.remove(lockName, localLock);
        }
    }

    /**
     * 远程释放锁
     */
    private boolean doRemoteUnlock(long id, String lockValue, long evictInterval) {
        // return globalLockAssistant.releaseLock(id, lockValue, evictInterval);
        return true;
    }

    /**
     * 定时锁续期
     */
    private void scheduleRenewalLock(String lockName, long lockId, String lockValue) {
        wheelScheduler.schedule(SCHEDULER_LOCK_KEY.replace("{lockName}", lockName).replace("{lockValue}",
                lockValue), () -> {
            // if (globalLockAssistant.renewalLock(lockId, lockValue, RENEWAL_LOCK_INTERVAL) && lockValues.contains
            // (lockValue)) {
            //     this.scheduleRenewalLock(lockName, lockId, lockValue);
            // }
        }, RENEWAL_LOCK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * 取消定时锁续期
     */
    private void cancelScheduleRenewalLock(String lockName, String lockValue) {
        wheelScheduler.cancel(SCHEDULER_LOCK_KEY.replace("{lockName}", lockName).replace("{lockValue}", lockValue));
    }

    @Data
    private static class RemoteLockInfo {
        private Integer reentrantCount = 0;
        private Long lockId;
        private String lockValue;
        private ReentrantLock associatedLocalLock;
    }
}

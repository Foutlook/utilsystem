package com.foutin.mysql.lock;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author XingKong
 * @Date 2023/6/7 14:08
 */
public enum DistributedLockModuleEnum implements IDistributedLockModule {

    /**
     * 清除 mysql 锁记录
     */
    EVICT_MYSQL_LOCK_RECORD,


    /**
     * 审批相关
     */
    AUDIT("audit"),

    /**
     * 生成新版本
     */
    NEW_VERSION("newVersion", 10 * 1000L),

    /**
     * 设置默认流程
     */
    SET_DEFAULT_MODEL("setDefaultModel"),

    /**
     * 表单更新
     */
    FORM_CENTER("formCenter"),

    ;

    /**
     * 锁名称
     */
    private final String lockName;
    /**
     * 轮询抢占锁间隔时间（单位：毫秒）
     */
    private final int pollGap;
    /**
     * 锁过期时间，-1 表示会定时续期（单位：毫秒）
     */
    private final int expireInterval;
    /**
     * 锁清除时间，-1 表示永不清除（单位：毫秒）
     */
    private final int evictInterval;
    /**
     * 锁等待时间，-1 表示永久等待（单位：毫秒）
     */
    private final long waitTime;

    DistributedLockModuleEnum() {
        this(null, DistributedLockConstants.DEFAULT_EVICT_INTERVAL, DistributedLockConstants.DEFAULT_WAIT_TIME);
    }

    DistributedLockModuleEnum(String lockName) {
        this(lockName, DistributedLockConstants.DEFAULT_EVICT_INTERVAL, DistributedLockConstants.DEFAULT_WAIT_TIME);
    }

    DistributedLockModuleEnum(int evictInterval) {
        this(null, evictInterval, DistributedLockConstants.DEFAULT_WAIT_TIME);
    }

    DistributedLockModuleEnum(long waitTime) {
        this(null, DistributedLockConstants.DEFAULT_EVICT_INTERVAL, waitTime);
    }

    DistributedLockModuleEnum(String lockName, int evictInterval) {
        this(lockName, evictInterval, DistributedLockConstants.DEFAULT_WAIT_TIME);
    }

    DistributedLockModuleEnum(String lockName, long waitTime) {
        this(lockName, DistributedLockConstants.DEFAULT_EVICT_INTERVAL, waitTime);
    }

    DistributedLockModuleEnum(String lockName, int evictInterval, long waitTime) {
        this(lockName, DistributedLockConstants.DEFAULT_POLL_GAP, DistributedLockConstants.DEFAULT_EXPIRE_INTERVAL,
                evictInterval, waitTime);
    }

    /**
     * @param lockName       锁名称
     * @param pollGap        轮询抢占锁间隔时间（单位：毫秒）
     * @param expireInterval 锁过期时间（单位：毫秒）
     * @param evictInterval  锁清除时间（单位：毫秒）
     * @param waitTime       锁等待时间（单位：毫秒）
     */
    DistributedLockModuleEnum(String lockName, Integer pollGap, Integer expireInterval, Integer evictInterval,
                              Long waitTime) {
        this.lockName = lockName;
        this.pollGap = pollGap != null ? pollGap : DistributedLockConstants.DEFAULT_POLL_GAP;
        this.expireInterval = expireInterval != null ? expireInterval :
                DistributedLockConstants.DEFAULT_EXPIRE_INTERVAL;
        this.evictInterval = evictInterval != null ? evictInterval : DistributedLockConstants.DEFAULT_EVICT_INTERVAL;
        this.waitTime = waitTime != null ? waitTime : DistributedLockConstants.DEFAULT_WAIT_TIME;
    }

    /**
     * 获取锁名称
     */
    @Override
    public String getLockName() {
        return StringUtils.isNotBlank(lockName) ? lockName : this.name();
    }

    /**
     * 获取轮询抢占锁间隔时间
     */
    @Override
    public int getPollGap() {
        return pollGap;
    }

    /**
     * 获取锁过期时间
     */
    @Override
    public int getExpireInterval() {
        return expireInterval;
    }

    /**
     * 获取锁清除时间
     */
    @Override
    public int getEvictInterval() {
        return evictInterval;
    }

    /**
     * 获取锁等待时间
     */
    @Override
    public long getWaitTime() {
        return waitTime;
    }

}

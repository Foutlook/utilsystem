package com.foutin.mysql.lock;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author XingKong
 * @Date 2023/6/21 16:27
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GlobalLockAssistant {

    // @Resource
    // private GlobalLockMapper globalLockMapper;

    /**
     * 插入锁
     */
    // public GlobalLockEntity insertLock(String lockName, String lockValue, int expireInterval) {
    //     long expireTime = System.currentTimeMillis() + expireInterval;
    //     GlobalLockEntity globalLockEntity = GlobalLockEntity.builder()
    //             .lockName(lockName)
    //             .lockFlag(true)
    //             .lockValue(lockValue)
    //             .version(0L)
    //             .evictTime(new Date(expireTime + Integer.MAX_VALUE * 2L))
    //             .expireTime(new Date(expireTime)).build();
    //     globalLockMapper.insert(globalLockEntity);
    //     return globalLockEntity;
    // }


    /**
     * 添加锁
     */
    // public boolean addLock(long id, String lockValue, int expireInterval, long version) {
    //     long expireTime = System.currentTimeMillis() + expireInterval;
    //     return globalLockMapper.update(GlobalLockEntity.builder()
    //                     .lockFlag(true)
    //                     .lockValue(lockValue)
    //                     .version(version + 1)
    //                     .evictTime(new Date(expireTime + Integer.MAX_VALUE * 2L))
    //                     .expireTime(new Date(expireTime)).build(),
    //             new LambdaQueryWrapper<GlobalLockEntity>()
    //                     .eq(GlobalLockEntity::getId, id)
    //                     .eq(GlobalLockEntity::getVersion, version)) > 0;
    // }

    /**
     * 释放锁
     */
    // public boolean releaseLock(long id, String lockValue, long evictInterval) {
    //     return globalLockMapper.update(GlobalLockEntity.builder()
    //                     .lockFlag(false)
    //                     .evictTime(new Date(System.currentTimeMillis() + evictInterval)).build(),
    //             new LambdaQueryWrapper<GlobalLockEntity>()
    //                     .eq(GlobalLockEntity::getId, id)
    //                     .eq(GlobalLockEntity::getLockValue, lockValue)) > 0;
    // }

    /**
     * 续期锁
     */
    // public boolean renewalLock(long id, String lockValue, int expireInterval) {
    //     return globalLockMapper.renewalLock(id, lockValue, expireInterval * 1000L) > 0;
    // }

    /**
     * 清除锁
     */
    // public void evictLock(long id) {
    //     Date currTime = new Date();
    //     globalLockMapper.delete(new LambdaQueryWrapper<GlobalLockEntity>()
    //             .eq(GlobalLockEntity::getId, id)
    //             .le(GlobalLockEntity::getEvictTime, currTime)
    //             .and(wrapper -> wrapper
    //                     .eq(GlobalLockEntity::getLockFlag, false)
    //                     .or()
    //                     .le(GlobalLockEntity::getExpireTime, currTime)));
    // }
}

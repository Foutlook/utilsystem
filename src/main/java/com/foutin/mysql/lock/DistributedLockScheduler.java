package com.foutin.mysql.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author XingKong
 * @Date 2023/6/8 17:22
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockScheduler {

    private final IDistributedLockManager distributedLockManager;

    // private final GlobalLockMapper globalLockMapper;

    private final GlobalLockAssistant globalLockAssistant;

    /**
     * 定时清理 MySQL 分布式锁记录
     */
    // @Scheduled(cron = "0 * 1-6 * * ?")
    // public void scheduleEvictMySqlLockRecord() {
    //     if (distributedLockManager.tryLock(DistributedLockModuleEnum.EVICT_MYSQL_LOCK_RECORD)) {
    //         try {
    //             Date currTime = new Date();
    //             List<GlobalLockEntity> globalLockEntities =
    //                     globalLockMapper.selectList(new LambdaQueryWrapper<GlobalLockEntity>()
    //                                     .le(GlobalLockEntity::getEvictTime, currTime)
    //                                     .and(wrapper -> wrapper
    //                                             .eq(GlobalLockEntity::getLockFlag, false)
    //                                             .or()
    //                                             .le(GlobalLockEntity::getExpireTime, currTime))
    //                                     .last("limit 100"));
    //             if (CollectionUtils.isEmpty(globalLockEntities)) {
    //                 return;
    //             }
    //             for (GlobalLockEntity globalLockEntity : globalLockEntities) {
    //                 globalLockAssistant.evictLock(globalLockEntity.getId());
    //             }
    //         } finally {
    //             distributedLockManager.unlock(DistributedLockModuleEnum.EVICT_MYSQL_LOCK_RECORD);
    //         }
    //     }
    // }
}

package com.foutin.mysql.lock;

import java.util.concurrent.TimeUnit;

/**
 * @Author XingKong
 * @Date 2023/6/7 19:13
 */
public interface IDistributedLockManager {

    void lock(DistributedLockModuleEnum lockModule);

    void lock(DistributedLockModuleEnum lockModule, String lockKey);

    void lock(String lockName);

    void lock(String lockName, long timeout, TimeUnit unit);

    boolean tryLock(DistributedLockModuleEnum lockModule);

    boolean tryLock(DistributedLockModuleEnum lockModule, String lockKey);

    boolean tryLock(String lockName);

    boolean unlock(DistributedLockModuleEnum lockModule);

    boolean unlock(DistributedLockModuleEnum lockModule, String lockKey);

    boolean unlock(String lockName);
}

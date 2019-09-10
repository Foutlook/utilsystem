package com.foutin.zookeeper.lock.interprocessmutex;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @author xingkai.fan
 * @description zk 实现分布式锁接口
 * @date 2019/9/4
 */
public interface DistributedZkLock {

    boolean sharedReentrantLock(Long waitTime);

    void sharedReentrantUnlock();

    InterProcessMutex sharedReentrantReadLock(Long waitTime);

    InterProcessMutex sharedReentrantWriteLock(Long waitTime);

    void sharedReentrantReadWriteUnlock(InterProcessMutex interProcessMutex);


}

package com.foutin.zookeeper.lock.interprocessmutex;

import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @author xingkai.fan
 * @description zk 实现分布式锁接口
 * @date 2019/9/4
 */
public interface DistributedZkLock {

    InterProcessMutex sharedReentrantLock(String path, Long waitTime);

    void sharedReentrantUnlock(InterProcessMutex interProcessMutex);

    InterProcessMutex sharedReentrantReadLock(String path, Long waitTime);

    InterProcessMutex sharedReentrantWriteLock(String path, Long waitTime);

    void sharedReentrantReadWriteUnlock(InterProcessMutex interProcessMutex);

    InterProcessMultiLock multiSharedLock(String path, Long waitTime);

    void multiSharedUnlock(InterProcessMultiLock interProcessMutex);

}

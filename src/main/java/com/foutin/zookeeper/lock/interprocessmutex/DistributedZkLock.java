package com.foutin.zookeeper.lock.interprocessmutex;

/**
 * @author xingkai.fan
 * @description zk 实现分布式锁接口
 * @date 2019/9/4
 */
public interface DistributedZkLock {

    boolean sharedReentrantLock(Long waitTime);

    void sharedReentrantUnlock();

    boolean sharedReentrantReadWriteLock(Long waitTime);

    // todo 释放锁

}

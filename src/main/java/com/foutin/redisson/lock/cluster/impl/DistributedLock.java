package com.foutin.redisson.lock.cluster.impl;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31
 */
public interface DistributedLock {

    /**
     * 以下redisson的锁实现方法
     * --------------------------------------------------------------
     */
    Boolean tryLock(String key) throws InterruptedException;

    Boolean tryLock(String key, Long expirationTime, TimeUnit timeUnit) throws InterruptedException;

    Boolean tryLock(String key, Long waitTime, Long expirationTime, TimeUnit timeUnit) throws InterruptedException;

    void unlock(String key);

}

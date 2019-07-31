package com.foutin.redisson.lock.cluster.impl;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31 9:32
 */
public abstract class AbstractDistributedLock implements DistributedLock {

    private long leaseTimeSecond = 15;

    private long waitTimeSecond = 2;

    /**
     * 以下redisson的锁实现方法
     * ---------------------------------------------------------------
     */
    @Override
    public Boolean tryLock(String key) throws InterruptedException {
        return tryLock(key, waitTimeSecond, leaseTimeSecond, TimeUnit.SECONDS);
    }

    @Override
    public Boolean tryLock(String key, Long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        return tryLock(key, waitTimeSecond, leaseTime, timeUnit);
    }
}

package com.foutin.rediss.lock.impl;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31 20:15
 */
public abstract class AbstractDistributedReantrantLock implements DistributedReentrantLock {

    private long timeoutMillis = 30000;

    private int retryTimes = Integer.MAX_VALUE;

    private long sleepMillis = 500;
    /**
     * 以下redis的锁实现方法
     * -----------------------------------------------------------------
     */
    @Override
    public boolean lock(String key) {
        return lock(key, timeoutMillis, retryTimes, sleepMillis);
    }

    @Override
    public boolean lock(String key, int retryTimes) {
        return lock(key, timeoutMillis, retryTimes, sleepMillis);
    }

    @Override
    public boolean lock(String key, int retryTimes, long sleepMillis) {
        return lock(key, timeoutMillis, retryTimes, sleepMillis);
    }

    @Override
    public boolean lock(String key, long expire) {
        return lock(key, expire, retryTimes, sleepMillis);
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes) {
        return lock(key, expire, retryTimes, sleepMillis);
    }

}

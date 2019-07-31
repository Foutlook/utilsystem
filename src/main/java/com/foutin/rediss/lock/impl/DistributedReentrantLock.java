package com.foutin.rediss.lock.impl;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31 20:14
 */
public interface DistributedReentrantLock {
    /**
     * 以下redis的锁实现方法
     * --------------------------------------------------------------
     */
    boolean lock(String key);

    boolean lock(String key, int retryTimes);

    boolean lock(String key, int retryTimes, long sleepMillis);

    boolean lock(String key, long expire);

    boolean lock(String key, long expire, int retryTimes);

    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    boolean releaseLock(String key);
}

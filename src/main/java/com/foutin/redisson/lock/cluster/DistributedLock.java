package com.foutin.redisson.lock.cluster;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31
 */
public interface DistributedLock {

    long EXPIRE_TIME_MILLIS = 15000;

    long WAIT_TIME_MILLIS = 1000;

    /**
     * 获取锁,默认等待时间和过期时间
     *
     * @param key 锁key
     * @return Boolean
     * @throws InterruptedException
     */
    Boolean tryLock(String key);

    /**
     * 获取锁，默认等待时间
     *
     * @param key        锁key
     * @param expireTime 过期时间
     * @param timeUnit   单位
     * @return Boolean
     * @throws InterruptedException
     */
    Boolean tryLock(String key, Long expireTime, TimeUnit timeUnit);

    /**
     * 获取锁，用户自定义
     *
     * @param key        锁key
     * @param waitTime   等待时间
     * @param expireTime 过期时间
     * @param timeUnit   单位
     * @return Boolean
     * @throws InterruptedException
     */
    Boolean tryLock(String key, Long waitTime, Long expireTime, TimeUnit timeUnit);

    /**
     * 解锁
     *
     * @param key 锁key
     */
    void unlock(String key);

}

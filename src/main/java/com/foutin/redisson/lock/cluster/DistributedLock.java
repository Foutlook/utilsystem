package com.foutin.redisson.lock.cluster;

import org.redisson.RedissonMultiLock;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31
 */
public interface DistributedLock {

    /**
     * 获取锁,默认等待时间和过期时间
     *
     * @param key 锁key
     * @return Boolean
     */
    Boolean tryLock(String key);

    /**
     * 获取锁，默认时间单位毫秒
     *
     * @param key        锁key
     * @param expireTime 过期时间
     * @param waitTime   等待时间
     * @return Boolean
     */
    Boolean tryLock(String key, Long waitTime, Long expireTime);

    /**
     * 获取锁，用户自定义
     *
     * @param key        锁key
     * @param waitTime   等待时间
     * @param expireTime 过期时间
     * @param timeUnit   单位
     * @return Boolean
     */
    Boolean tryLock(String key, Long waitTime, Long expireTime, TimeUnit timeUnit);

    /**
     * 解锁
     *
     * @param key 锁key
     */
    void unlock(String key);

    /**
     * 获取联锁,默认等待时间和过期时间
     *
     * @param keys 锁集合
     * @return 联锁对象
     */
    RedissonMultiLock tryMultiLock(List<String> keys);

    /**
     * 获取联锁,默认单位毫秒
     * @param keys
     * @param waitTime
     * @param expireTime
     * @return
     */
    RedissonMultiLock tryMultiLock(List<String> keys, Long waitTime, Long expireTime);

    /**
     * 对多个key同时加锁,获取联锁
     *
     * @param keys       key集合
     * @param waitTime   等待时间
     * @param expireTime 过期时间
     * @param timeUnit   单位
     * @return 联锁对象
     */
    RedissonMultiLock tryMultiLock(List<String> keys, Long waitTime, Long expireTime, TimeUnit timeUnit);

}

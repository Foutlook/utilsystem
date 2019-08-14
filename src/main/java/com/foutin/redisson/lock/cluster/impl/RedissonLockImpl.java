package com.foutin.redisson.lock.cluster.impl;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/26 18:11
 */
public class RedissonLockImpl extends AbstractDistributedLock {
    private static Logger log = LoggerFactory.getLogger(RedissonLockImpl.class);

    private RedissonClient redissonClient;
    private static final String LOCK_KEY = "lockKey_";

    public RedissonLockImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private RLock init(String key) {
        return redissonClient.getFairLock(LOCK_KEY + key);
    }

    @Override
    public Boolean tryLock(String key, Long waitTimeSeconds, Long expirationSeconds, TimeUnit unit) {
        RLock lock = init(key);
        boolean locked = false;
        try {
            locked = lock.tryLock(waitTimeSeconds, expirationSeconds, unit);
        } catch (InterruptedException e) {
            log.warn("获取锁失败", e);
        }
        return locked;
    }

    @Override
    public void unlock(String key) {
        RLock lock = init(key);
        lock.unlock();
    }
}

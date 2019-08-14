package com.foutin.redisson.lock.cluster.impl;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
        boolean locked = lock.isLocked();
        if (locked) {
            lock.unlock();
        }
    }

    @Override
    public RedissonMultiLock tryMultiLock(List<String> keys, Long waitTime, Long expireTime, TimeUnit timeUnit) {
        RedissonMultiLock multiLock = getMultiLock(keys);
        boolean locked = false;
        try {
            locked = multiLock.tryLock(waitTime, expireTime, timeUnit);
        } catch (InterruptedException e) {
            log.warn("获取联锁失败", e);
        }

        if (!locked) {
            log.warn("无法获取联锁");
            throw new RuntimeException("无法获取联锁");
        }
        return multiLock;
    }

    private RedissonMultiLock getMultiLock(List<String> keys) {
        List<RLock> rLockList = new ArrayList<>();
        for (String key : keys) {
            rLockList.add(init(key));
        }
        return new RedissonMultiLock(rLockList.toArray(new RLock[0]));
    }
}

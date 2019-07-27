package com.foutin.redisson.lock.cluster;

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
public class RedissonLockUtils {
    private static Logger log = LoggerFactory.getLogger(RedissonLockUtils.class);

    private RedissonClient redissonClient;
    private static final String LOCK_KEY = "lockKey_";
    private RLock lock;

    public RedissonLockUtils(RedissonClient redissonClient, String key) {
        this.redissonClient = redissonClient;
        this.lock = init(key);
    }

    private RLock init(String key) {
        log.info(">>>初始化锁对象...");
        return redissonClient.getLock(LOCK_KEY + key);
    }

    public Boolean tryLock(Long waitTimeSeconds, Long expirationSeconds) throws InterruptedException {
        log.info(">>>开始获取锁...");
        return lock.tryLock(waitTimeSeconds, expirationSeconds, TimeUnit.SECONDS);
    }

    void unLock() {
        log.info(">>>开始解锁...");
        lock.unlock();
    }

}

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
    private Long waitTimeSeconds;
    private Long expirationSeconds;
    private RLock lock;

    public RedissonLockUtils(RedissonClient redissonClient, String key) {
        this.redissonClient = redissonClient;
        this.lock = init(key);
    }

    private RLock init(String key) {
        log.info(">>>初始化锁对象...");
        return redissonClient.getFairLock(LOCK_KEY + key);
    }

    Boolean tryLock(Long waitTimeSeconds, Long expirationSeconds) throws InterruptedException {
        this.waitTimeSeconds = waitTimeSeconds;
        this.expirationSeconds = expirationSeconds;
        log.info(">>>开始获取锁...");
        return lock.tryLock(waitTimeSeconds, expirationSeconds, TimeUnit.SECONDS);
    }

    void unLock() {
        log.info(">>>开始解锁...");
        lock.unlock();
    }

    /**
     * 重试获取锁
     * @param time 重试次数
     * @param sleepMillis 睡眠时间
     * @return
     * @throws InterruptedException
     */
    Boolean retryLock(int time, long sleepMillis) throws InterruptedException {
        log.info(">>>开始重试获取锁...");
        boolean locked;
        while (time > 0) {
            locked = lock.tryLock(waitTimeSeconds, expirationSeconds, TimeUnit.SECONDS);
            if (locked) {
                log.info(">>>重试获取锁成功...");
                return true;
            }
            time--;
            Thread.sleep(sleepMillis);
        }

        // 无限次重试
        while (time < 0) {
            locked = lock.tryLock(waitTimeSeconds, expirationSeconds, TimeUnit.SECONDS);
            if (locked) {
                log.info(">>>重试获取锁成功...");
                return true;
            }
            Thread.sleep(sleepMillis);
        }
        log.info(">>>重试获取锁失败...");
        return false;
    }
}

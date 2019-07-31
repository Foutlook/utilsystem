package com.foutin.redisson.lock.cluster;

import com.foutin.redisson.lock.cluster.impl.AbstractDistributedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/26 18:11
 */
@Component
public class RedissonLockUtils extends AbstractDistributedLock {
    private static Logger log = LoggerFactory.getLogger(RedissonLockUtils.class);

    @Autowired
    private RedissonClient redissonClient;
    private static final String LOCK_KEY = "lockKey_";

    private RLock init(String key) {
        return redissonClient.getFairLock(LOCK_KEY + key);
    }

    @Override
    public Boolean tryLock(String key, Long waitTimeSeconds, Long expirationSeconds, TimeUnit unit) throws InterruptedException {
        log.info(">>>开始获取锁...");
        RLock lock = init(key);
        return lock.tryLock(waitTimeSeconds, expirationSeconds, unit);
    }

    @Override
    public void unlock(String key) {
        log.info(">>>开始解锁...");
        RLock lock = init(key);
        lock.unlock();
    }
}

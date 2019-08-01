package com.foutin.redisson.lock.cluster.impl;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31 9:32
 */
public abstract class AbstractDistributedLock implements DistributedLock {

    @Override
    public Boolean tryLock(String key) {
        return tryLock(key, WAIT_TIME_MILLIS, EXPIRE_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean tryLock(String key, Long expirationTime, TimeUnit timeUnit) {
        return tryLock(key, WAIT_TIME_MILLIS, expirationTime, timeUnit);
    }
}

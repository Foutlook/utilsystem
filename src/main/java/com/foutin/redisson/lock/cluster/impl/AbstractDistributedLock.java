package com.foutin.redisson.lock.cluster.impl;

import com.foutin.redisson.lock.cluster.DistributedLock;
import com.foutin.redisson.lock.cluster.constant.LockConstants;
import org.redisson.RedissonMultiLock;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/31 9:32
 */
public abstract class AbstractDistributedLock implements DistributedLock {

    @Override
    public Boolean tryLock(String key) {
        return tryLock(key, LockConstants.WAIT_TIME_MILLIS, LockConstants.EXPIRE_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean tryLock(String key, Long waitTime, Long expirationTime) {
        return tryLock(key, waitTime, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public RedissonMultiLock tryMultiLock(List<String> keys) {
        return tryMultiLock(keys, LockConstants.WAIT_TIME_MILLIS, LockConstants.EXPIRE_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

}

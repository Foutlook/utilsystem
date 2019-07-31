package com.foutin.redisson.lock.cluster;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description TODO
 * @date 2019/7/31
 */
public interface DistributedLock {

    Boolean tryLock() throws InterruptedException;

    Boolean tryLock(Long leaseTime, TimeUnit timeUnit) throws InterruptedException;

    Boolean tryLock(Long waitTime, Long leaseTime, TimeUnit timeUnit) throws InterruptedException;

    void unlock();

}

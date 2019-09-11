package com.foutin.zookeeper.lock.service;

import com.foutin.zookeeper.lock.interprocessmutex.DistributedZkLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/9/4 18:14
 */
@Service
public class ZKDemoLockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZKDemoLockService.class);

    private final static String SHARED_REENTRANT_LOCK_PATH = "/sharedReentrantLock";
    private final static String SHARED_READ_WRITE_LOCK_PATH = "/sharedReadWriteLock";
    private final static String SHARED_MULTI_LOCK_PATH = "/sharedMultiLock";

    @Autowired
    private DistributedZkLock distributedZkLock;

    public void demoReentrantLock() {
        InterProcessMutex interProcessMutex = distributedZkLock.sharedReentrantLock(SHARED_REENTRANT_LOCK_PATH, 1000L);
        System.out.println("fanxingkai-demoLockService:");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.info("获取锁失败", e);
            e.printStackTrace();
        } finally {
            distributedZkLock.sharedReentrantUnlock(interProcessMutex);
        }
    }

    public void demoReadWriteLock() {
        InterProcessMutex interProcessMutex = distributedZkLock.sharedReentrantReadLock(SHARED_READ_WRITE_LOCK_PATH, 1000L);
        System.out.println("fanxingkai-demoLockService:");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.info("获取锁失败", e);
            e.printStackTrace();
        } finally {
            distributedZkLock.sharedReentrantReadWriteUnlock(interProcessMutex);
        }
    }
}

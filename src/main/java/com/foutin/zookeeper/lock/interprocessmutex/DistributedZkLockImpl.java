package com.foutin.zookeeper.lock.interprocessmutex;

import com.foutin.zookeeper.lock.ZookeeperClient;
import com.foutin.zookeeper.lock.ZookeeperClientImpl;
import com.foutin.zookeeper.lock.ZookeeperConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description 使用InterProcessMutex实现zookeeper分布式锁
 * @date 2019/9/4 14:24
 */
@Service
public class DistributedZkLockImpl implements DistributedZkLock{

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedZkLockImpl.class);

    @Autowired
    private ZookeeperClient zookeeperClient;

    @Override
    public boolean sharedReentrantLock(Long waitTime) {
        InterProcessMutex interProcessMutex = zookeeperClient.newInterProcessMutex();
        boolean acquire;
        try {
            acquire = interProcessMutex.acquire(waitTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.warn("获取zk锁失败", e);
            throw new RuntimeException(e);
        }
        return acquire;
    }

    @Override
    public void sharedReentrantUnlock() {
        InterProcessMutex interProcessMutex = zookeeperClient.newInterProcessMutex();
        try {
            interProcessMutex.release();
        } catch (Exception e) {
            LOGGER.warn("解锁失败", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public InterProcessMutex sharedReentrantReadLock(Long waitTime) {
        InterProcessReadWriteLock interProcessReadWriteLock = zookeeperClient.newInterProcessReadWriteLock();
        InterProcessMutex interProcessMutex = interProcessReadWriteLock.readLock();
        try {
            boolean acquire = interProcessMutex.acquire(waitTime, TimeUnit.MILLISECONDS);
            if (!acquire) {
                LOGGER.warn("获取读锁失败");
                throw new RuntimeException("获取读锁失败");
            }
        } catch (Exception e) {
            LOGGER.warn("获取zk读锁失败", e);
            throw new RuntimeException(e);
        }
        return interProcessMutex;
    }

    @Override
    public InterProcessMutex sharedReentrantWriteLock(Long waitTime) {
        InterProcessReadWriteLock interProcessReadWriteLock = zookeeperClient.newInterProcessReadWriteLock();
        InterProcessMutex interProcessMutex = interProcessReadWriteLock.writeLock();
        try {
            boolean acquire = interProcessMutex.acquire(waitTime, TimeUnit.MILLISECONDS);
            if (!acquire) {
                LOGGER.warn("获取写锁失败");
                throw new RuntimeException("获取写锁失败");
            }
        } catch (Exception e) {
            LOGGER.warn("获取zk读锁失败", e);
            throw new RuntimeException(e);
        }
        return interProcessMutex;
    }

    @Override
    public void sharedReentrantReadWriteUnlock(InterProcessMutex interProcessMutex) {
        try {
            interProcessMutex.release();
        } catch (Exception e) {
            LOGGER.warn("读写锁解锁失败", e);
            throw new RuntimeException(e);
        }
    }
}

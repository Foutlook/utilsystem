package com.foutin.zookeeper.lock.interprocessmutex;

import com.foutin.zookeeper.lock.ZookeeperClient;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
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
    public InterProcessMutex sharedReentrantLock(String path, Long waitTime) {
        InterProcessMutex interProcessMutex = zookeeperClient.newInterProcessMutex(path);
        boolean acquire;
        try {
            acquire = interProcessMutex.acquire(waitTime, TimeUnit.MILLISECONDS);
            if (!acquire) {
                LOGGER.warn("获取锁失败");
                throw new RuntimeException("获取锁失败");
            }
        } catch (Exception e) {
            LOGGER.warn("获取zk锁失败", e);
            throw new RuntimeException(e);
        }
        return interProcessMutex;
    }

    @Override
    public void sharedReentrantUnlock(InterProcessMutex interProcessMutex) {
        try {
            interProcessMutex.release();
        } catch (Exception e) {
            LOGGER.warn("解锁失败", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public InterProcessMutex sharedReentrantReadLock(String path, Long waitTime) {
        InterProcessReadWriteLock interProcessReadWriteLock = zookeeperClient.newInterProcessReadWriteLock(path);
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
    public InterProcessMutex sharedReentrantWriteLock(String path, Long waitTime) {
        InterProcessReadWriteLock interProcessReadWriteLock = zookeeperClient.newInterProcessReadWriteLock(path);
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

    @Override
    public InterProcessMultiLock multiSharedLock(String path, Long waitTime) {
        InterProcessMultiLock interProcessMultiLock = zookeeperClient.newInterProcessMultiLock(path);
        try {
            boolean acquire = interProcessMultiLock.acquire(waitTime, TimeUnit.MICROSECONDS);
            if (!acquire) {
                LOGGER.warn("获取多锁失败");
                throw new RuntimeException("获取多锁失败");
            }
        } catch (Exception e) {
            LOGGER.warn("获取zk多锁失败");
            throw new RuntimeException("获取zk多锁失败");
        }
        return interProcessMultiLock;
    }

    @Override
    public void multiSharedUnlock(InterProcessMultiLock interProcessMultiLock) {
        try {
            interProcessMultiLock.release();
        } catch (Exception e) {
            LOGGER.warn("多锁锁解锁失败", e);
            throw new RuntimeException(e);
        }
    }
}

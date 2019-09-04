package com.foutin.zookeeper.lock.interprocessmutex;

import com.foutin.zookeeper.lock.ZookeeperClient;
import com.foutin.zookeeper.lock.ZookeeperConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
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
    public void sharedReentrantLock(Long waitTime) {
        InterProcessMutex interProcessMutex = zookeeperClient.newInterProcessMutex();
        try {
            boolean acquire = interProcessMutex.acquire(waitTime, TimeUnit.MILLISECONDS);
            if (!acquire) {
                LOGGER.warn("获取锁失败");
            }
        } catch (Exception e) {
            LOGGER.warn("获取zk锁失败:{}", e.getMessage());
        }
    }

    @Override
    public void sharedReentrantReadWriteLock(Long waitTime) {
    }
}

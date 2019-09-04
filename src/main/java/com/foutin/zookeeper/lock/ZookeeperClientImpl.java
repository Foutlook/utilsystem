package com.foutin.zookeeper.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xingkai.fan
 * @description zk 工具类
 * @date 2019/9/4 14:59
 */
@Service
public class ZookeeperClientImpl implements ZookeeperClient {

    @Autowired
    private ZookeeperConfig zookeeperConfig;

    @Override
    public CuratorFramework newClient() {
        String zkServerLists = zookeeperConfig.getZkServerLists();
        String zkRetryPolicySleepTimeMs = zookeeperConfig.getZkRetryPolicySleepTimeMs();
        String zkMaxRetries = zookeeperConfig.getZkMaxRestries();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Integer.valueOf(zkRetryPolicySleepTimeMs), Integer.valueOf(zkMaxRetries));
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(zkServerLists, retryPolicy);
        curatorFramework.start();
        return curatorFramework;
    }

    @Override
    public InterProcessMutex newInterProcessMutex() {
        // todo 不能这样做
        CuratorFramework curatorFramework = newClient();
        return new InterProcessMutex(curatorFramework, zookeeperConfig.getZkReentrantLockPath());
    }

    @Override
    public InterProcessReadWriteLock newInterProcessReadWriteLock() {
        CuratorFramework curatorFramework = newClient();
        return new InterProcessReadWriteLock(curatorFramework, zookeeperConfig.getZkReadWriteLockPath());
    }

    @Override
    public InterProcessMultiLock newInterProcessMultiLock() {
        final String separator = ",";
        CuratorFramework curatorFramework = newClient();
        String zkSharedMultiLock = zookeeperConfig.getZkSharedMultiLock();
        List<String> multiLockList = new ArrayList<>();
        if (zkSharedMultiLock.contains(separator)) {
            String[] arrays = zkSharedMultiLock.split(",");
            List<String> multiLocks = Stream.of(arrays).collect(Collectors.toList());
            multiLockList.addAll(multiLocks);
        }
        if (ObjectUtils.isEmpty(multiLockList)) {
            multiLockList.add(zkSharedMultiLock);
        }
        return new InterProcessMultiLock(curatorFramework, multiLockList);
    }

}

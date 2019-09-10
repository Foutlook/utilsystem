package com.foutin.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ZookeeperClientImpl implements ZookeeperClient {

    @Autowired
    private ZookeeperConfig zookeeperConfig;

    private CuratorFramework curatorFramework;

    public ZookeeperClientImpl(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    /**
     * 初始化操作
     */
    public void init(){
        // 使用命名空间
        curatorFramework  = curatorFramework.usingNamespace(zookeeperConfig.getZkNameSpace());
    }

    @Override
    public InterProcessMutex newInterProcessMutex() {
        return new InterProcessMutex(curatorFramework, zookeeperConfig.getZkReentrantLockPath());
    }

    @Override
    public InterProcessReadWriteLock newInterProcessReadWriteLock() {
        return new InterProcessReadWriteLock(curatorFramework, zookeeperConfig.getZkReadWriteLockPath());
    }

    @Override
    public InterProcessMultiLock newInterProcessMultiLock() {
        final String separator = ",";
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

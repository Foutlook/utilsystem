package com.foutin.zookeeper.lock.interprocessmutex.utils;

import com.foutin.zookeeper.lock.interprocessmutex.utils.ZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
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

    private String nameSpace;

    private CuratorFramework curatorFramework;

    public ZookeeperClientImpl(CuratorFramework curatorFramework, String nameSpace) {
        this.curatorFramework = curatorFramework;
        this.nameSpace = nameSpace;
    }

    /**
     * 初始化操作，使用命名空间
     */
    public void init() {
        curatorFramework = curatorFramework.usingNamespace(nameSpace);
    }

    @Override
    public InterProcessMutex newInterProcessMutex(String lockPath) {
        return new InterProcessMutex(curatorFramework, lockPath);
    }

    @Override
    public InterProcessReadWriteLock newInterProcessReadWriteLock(String lockPath) {
        return new InterProcessReadWriteLock(curatorFramework, lockPath);
    }

    @Override
    public InterProcessMultiLock newInterProcessMultiLock(String lockPath) {
        if (ObjectUtils.isEmpty(lockPath)) {
            throw new RuntimeException("Path cannot be null");
        }
        final String separator = ",";
        List<String> multiLockList = new ArrayList<>();
        if (lockPath.contains(separator)) {
            String[] arrays = lockPath.split(",");
            List<String> multiLocks = Stream.of(arrays).collect(Collectors.toList());
            multiLockList.addAll(multiLocks);
        }
        if (ObjectUtils.isEmpty(multiLockList)) {
            multiLockList.add(lockPath);
        }
        return new InterProcessMultiLock(curatorFramework, multiLockList);
    }

}

package com.foutin.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.List;

/**
 * @author xingkai.fan
 * @description zk 接口
 * @date 2019/9/4 15:02
 */
public interface ZookeeperClient {

    CuratorFramework newClient();

    InterProcessMutex newInterProcessMutex();

    InterProcessReadWriteLock newInterProcessReadWriteLock();

    InterProcessMultiLock newInterProcessMultiLock();

}

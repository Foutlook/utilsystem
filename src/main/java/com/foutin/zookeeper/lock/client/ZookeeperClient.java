package com.foutin.zookeeper.lock.client;

import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

/**
 * @author xingkai.fan
 * @description zk 接口
 * @date 2019/9/4 15:02
 */
public interface ZookeeperClient {

    InterProcessMutex newInterProcessMutex(String lockPath);

    InterProcessReadWriteLock newInterProcessReadWriteLock(String lockPath);

    InterProcessMultiLock newInterProcessMultiLock(String lockPath);

}

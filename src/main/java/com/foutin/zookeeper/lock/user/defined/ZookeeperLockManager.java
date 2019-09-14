package com.foutin.zookeeper.lock.user.defined;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;

public interface ZookeeperLockManager {

    String createNote(String path, byte[] localLockNodeBytes) throws Exception;

    NodeCache lockNodeWatch(String path);

    void closeNodeWatch(NodeCache cache);

    CuratorFramework getCuratorFramework();
}

package com.foutin.zookeeper.lock.user.defined;


import org.apache.curator.framework.recipes.cache.NodeCache;

public interface CustomZookeeperLock {

    void createNote(String path) throws Exception;

    NodeCache lockNodeWatch(String path);

    void closeNodeWatch(NodeCache cache);
}

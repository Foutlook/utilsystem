package com.foutin.zookeeper.lock.user.defined;


public interface CustomZookeeperLock {

    void createNote(String path) throws Exception;
}

package com.foutin.zookeeper.lock.user.defined;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface CustomZookeeperLock {

    void acquire() throws Exception;

    boolean acquire(long time, TimeUnit unit) throws Exception;

    LockInternals newLockInternals(String path);
}

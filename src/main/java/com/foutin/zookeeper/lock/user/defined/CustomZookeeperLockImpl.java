package com.foutin.zookeeper.lock.user.defined;

import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xingkai.fan
 * @date 2019-09-11
 */
public class CustomZookeeperLockImpl implements CustomZookeeperLock {
    private static final Logger logger = LoggerFactory.getLogger(CustomZookeeperLockImpl.class);

    /**
     * 记录线程与锁信息的映射关系
     */
    private final ConcurrentMap<Thread, LockData> threadData = Maps.newConcurrentMap();

    @Autowired
    private LockInternals lockInternals;

    @Override
    public void acquire() throws Exception {
        if ( !internalLock(-1, null) ){
            throw new IOException("Lost connection while trying to acquire lock: failure");
        }
    }

    @Override
    public boolean acquire(long time, TimeUnit unit) throws Exception {
        return internalLock(time, unit);
    }

    @Override
    public LockInternals newLockInternals(String path) {
        lockInternals.setPath(path);
        return lockInternals;
    }

    private boolean internalLock(long time, TimeUnit unit) throws Exception {
        Thread currentThread = Thread.currentThread();
        LockData lockData = threadData.get(currentThread);
        if ( lockData != null ){
            // 实现可重入
            // 同一线程再次acquire，首先判断当前的映射表内（threadData）是否有该线程的锁信息，如果有则原子+1，然后返回
            lockData.lockCount.incrementAndGet();
            return true;
        }

        // 映射表内没有对应的锁信息，尝试通过LockInternals获取锁
        String lockPath = lockInternals.attemptLock(time, unit, getLockNodeBytes());
        if ( lockPath != null ){
            // 成功获取锁，记录信息到映射表
            LockData newLockData = new LockData(currentThread, lockPath);
            threadData.put(currentThread, newLockData);
            return true;
        }
        return false;
    }

    protected byte[] getLockNodeBytes() {
        return null;
    }

    private static class LockData {
        final Thread owningThread;
        final String lockPath;
        final AtomicInteger lockCount;

        private LockData(Thread owningThread, String lockPath) {
            this.lockCount = new AtomicInteger(1);
            this.owningThread = owningThread;
            this.lockPath = lockPath;
        }
    }
}

package com.foutin.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author f2485
 * @Description
 * @date 2025/2/13 17:25
 */
public class SimpleMockLock implements Lock {


    // 同步器实例
    private final Sync sync = new Sync();


    // 自定义的内部类：同步器
    // 直接使用 AbstractQueuedSynchronizer.state 值表示锁的状态
    // AbstractQueuedSynchronizer.state=1 表示 锁没有被占用
    // AbstractQueuedSynchronizer.state=0 表示 锁没已经被占用
    private static class Sync extends AbstractQueuedSynchronizer {
        // 钩子方法
        protected boolean tryAcquire(int arg) {// CAS 更新状态值为 1
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 钩子方法
        protected boolean tryRelease(int arg) {
            // 如果当前线程不是占用锁的线程
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                // 抛出非法状态的异常
                throw new IllegalMonitorStateException();
            }
            // 如果锁的状态为没有占用
            if (getState() == 0) {
                // 抛出非法状态的异常
                throw new IllegalMonitorStateException();
            }
            // 接下来不需要使用 CAS 操作，以为下面的操作不存在并发场景
            setExclusiveOwnerThread(null);
            // 设置状态
            setState(0);
            return true;
        }
    }


    @Override
    public void lock() {
// 委托给同步器的 acquire 抢占方法
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
// 委托给同步器的 release 释放方法
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}

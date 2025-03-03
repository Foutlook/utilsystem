package com.foutin.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.foutin.nio.netty.simple.cray.im.cocurrent.ThreadUtil.sleepSeconds;

/**
 * @author f2485
 * @Description
 * @date 2025/2/14 14:30
 */
public class ReentrantCommunicationTest {
    // 创建一个显式锁
    static Lock lock = new ReentrantLock();
    // 获取一个显式锁绑定的 Condition 对象
    static private Condition condition = lock.newCondition();

    // 等待线程的异步目标任务
    static class WaitTarget implements Runnable {
        public void run() {
            lock.lock(); // ①抢锁
            try {
                System.out.println("我是等待方");
                condition.await(); // ② 开始等待,并且释放锁
                System.out.println("收到通知，等待方继续执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();// 释放锁
            }
        }
    }

    // 通知线程的异步目标任务
    static class NotifyTarget implements Runnable {
        public void run() {
            lock.lock(); //③抢锁
            try {
                System.out.println("我是通知方");
                condition.signal(); // ④发送通知
                System.out.println("发出通知了，但是线程还没有立马释放锁");
            } finally {
                lock.unlock(); //⑤释放锁之后，等待线程才能获得所
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建等待线程
        Thread waitThread = new Thread(new WaitTarget(), "WaitThread");
        // 启动等待线程
        waitThread.start();
        sleepSeconds(1); // 稍等一下
        // 创建通知线程
        Thread notifyThread = new Thread(new NotifyTarget(),
                "NotifyThread");
        // 启动通知线程
        notifyThread.start();
    }
}

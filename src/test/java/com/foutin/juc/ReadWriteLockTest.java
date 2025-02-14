package com.foutin.juc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author f2485
 * @Description
 * @date 2025/2/13 15:27
 */
public class ReadWriteLockTest {

    final static Map<String, String> MAP = new HashMap<String, String>();
    // 创建一个读写锁
    final static ReentrantReadWriteLock LOCK = new
            ReentrantReadWriteLock();
    // 获取读锁
    final static Lock READ_LOCK = LOCK.readLock();
    // 获取写锁
    final static Lock WRITE_LOCK = LOCK.writeLock();

    // 对共享数据的写操作
    public static Object put(String key, String value) {
        WRITE_LOCK.lock(); // 抢写锁
        try {
            System.out.println(new Date()
                    + " 抢占了 WRITE_LOCK，开始执行 write 操作");
            Thread.sleep(1000);
            String put = MAP.put(key, value); // 写入共享数据
            return put;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            WRITE_LOCK.unlock(); // 释放写锁
        }
        return null;
    }

    // 对共享数据的读操作
    public static Object get(String key) {
        READ_LOCK.lock(); // 抢占读锁
        try {
            System.out.println(new Date()
                    + " 抢占了 READ_LOCK，开始执行 read 操作");
            Thread.sleep(1000);
            String value = MAP.get(key); // 读取共享数据
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            READ_LOCK.unlock(); // 释放读锁
        }
        return null;
    }

    // 入口方法
    public static void main(String[] args) {
        // 创建 Runnable 异步可执行目标实例
        Runnable writeTarget = () -> put("key", "value");
        Runnable readTarget = () -> get("key");
        // 创建 4 条读线程
        for (int i = 0; i < 4; i++) {
            new Thread(readTarget, "读线程" + i).start();
        }
        // 创建 2 条写线程，并启动
        for (int i = 0; i < 2; i++) {
            new Thread(writeTarget, "写线程" + i).start();
        }
    }

}

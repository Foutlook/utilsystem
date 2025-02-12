package com.foutin.juc.lock;

import java.util.concurrent.locks.Lock;

/**
 * @author f2485
 * @Description
 * @date 2025/2/12 17:13
 */
public class IncrementData {
    public static int sum = 0;

    public static void lockAndFastIncrease(Lock lock) {
        System.out.println(" -- 开始抢占锁");
        lock.lock();
        try {
            System.out.println(" ^-^ 抢到了锁");
            sum++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

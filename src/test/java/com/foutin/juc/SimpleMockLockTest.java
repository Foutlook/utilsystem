package com.foutin.juc;

import com.foutin.juc.lock.IncrementData;
import com.foutin.juc.lock.SimpleMockLock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * @author f2485
 * @Description
 * @date 2025/2/13 17:28
 */
public class SimpleMockLockTest {

    @org.junit.Test
    public void testMockLock() {
        // 每条线程的执行轮数
        final int TURNS = 1000;
        // 线程数
        final int THREADS = 10;
        // 线程池，用于多线程模拟测试
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        // 自定义的独占锁
        Lock lock = new SimpleMockLock();
        // 倒数闩
        CountDownLatch countDownLatch = new CountDownLatch(THREADS);
        long start = System.currentTimeMillis();
        // 10 条线程并发执行
        for (int i = 0; i < THREADS; i++) {
            pool.submit(() ->
            {
                try {
                    // 累加 1000 次
                    for (int j = 0; j < TURNS; j++) {
                        // 传入锁，执行一次累加
                        IncrementData.lockAndFastIncrease(lock);
                    }
                    System.out.println("本线程累加完成");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 线程执行完成，倒数闩减少一次
                countDownLatch.countDown();
            });
        }
        try {
            // 等待倒数闩归 0，所有线程结束
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        float time = (System.currentTimeMillis() - start) / 1000F;
        // 输出统计结果
        System.out.println("运行的时长为：" + time);
        System.out.println("累加结果为：" + IncrementData.sum);
    }
}

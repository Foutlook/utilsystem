package com.foutin.juc;

import com.foutin.juc.lock.CLHLock;
import com.foutin.juc.lock.IncrementData;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

/**
 * @author f2485
 * @Description
 * @date 2025/2/12 17:11
 */
public class LockTest {

    @org.junit.Test
    public void testCLHLockCapability() {
        // 速度对比
        // ReentrantLock 1 000 000 次 0.154 秒
        // CLHLock 1 000 000 次 2.798 秒
        // 每条线程的执行轮数
        final int TURNS = 100000;
        // 线程数
        final int THREADS = 10;
        //线程池，用于多线程模拟测试
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        Lock lock = new CLHLock();
        // Lock lock = new ReentrantLock();
        // 倒数闩
        CountDownLatch countDownLatch = new CountDownLatch(THREADS);
        long start = System.currentTimeMillis();
        for (int i = 0; i < THREADS; i++)
        {
            pool.submit(() ->
            {
                for (int j = 0; j < TURNS; j++)
                {
                    IncrementData.lockAndFastIncrease(lock);
                }
                System.out.println("本线程累加完成");
                //倒数闩减少 1 次
                countDownLatch.countDown();
            });
        }
        try
        {
            //等待倒数闩归 0，所有线程结束
            countDownLatch.await();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        float time = (System.currentTimeMillis() - start) / 1000F;
        //输出统计结果
        System.out.println("运行的时长为：" + time);
        System.out.println("累加结果为：" + IncrementData.sum);
    }
}

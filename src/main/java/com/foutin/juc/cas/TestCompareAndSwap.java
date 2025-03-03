package com.foutin.juc.cas;

import com.foutin.nio.netty.simple.cray.im.cocurrent.ThreadUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static sun.misc.Unsafe.getUnsafe;


/**
 * @author f2485
 * @Description
 * @date 2025/2/14 10:56
 */
public class TestCompareAndSwap {

    // 并发数量
    private static final int THREAD_COUNT = 10;

    private static final Unsafe unsafe;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // 基于 CAS 无锁实现的安全自增
    static class OptimisticLockingPlus {

        // 内部值，使用 volatile 保证线程可见性
        private volatile int value;// 值

        // value 的内存偏移（相对与对象头部的偏移，不是绝对偏移）
        private static final long valueOffset;
        // 统计失败的次数
        private static final AtomicLong failure = new AtomicLong(0);

        static {
            try {
                // 取得 value 属性的内存偏移
                valueOffset = unsafe.objectFieldOffset(
                        OptimisticLockingPlus.class.getDeclaredField("value"));
                System.out.println("valueOffset:=" + valueOffset);
            } catch (Exception ex) {
                throw new Error(ex);
            }
        }

        // 通过 CAS 原子操作，进行“比较并交换”
        public final boolean unSafeCompareAndSet(int oldValue, int newValue) {
            // 原子操作：使用 unsafe 的“比较并交换方法”，进行 value 属性的交换
            return unsafe.compareAndSwapInt(
                    this, valueOffset, oldValue, newValue);
        }

        // 使用无锁编程实现：安全的自增方法
        public void selfPlus() {
            int oldValue = value;
            // 通过 CAS 原子操作，如果操作失败，则自旋，一直到操作成功
            do {
                // 获取旧值
                oldValue = value;
                // 记录失败的次数
                // failure.incrementAndGet();
            } while (!unSafeCompareAndSet(oldValue, oldValue + 1));
        }

    }
    // 测试用例入口方法
    public static void main(String[] args) throws InterruptedException {
        final OptimisticLockingPlus cas = new OptimisticLockingPlus();
        // 倒数闩，需要倒数 THREAD_COUNT 次
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            // 提交 10 个任务
            ThreadUtil.getMixedTargetThreadPool().submit(() ->
            {
                // 每个任务累加 1000 次
                for (int j = 0; j < 1000; j++) {
                    cas.selfPlus();
                }
                latch.countDown(); // 执行完一个任务，倒数闩减少一次
            });
        }
        latch.await(); // 主线程等待倒数闩倒数完毕
        System.out.println("累加之和：" + cas.value);
        // System.out.println("失败次数：" + cas.failure.get());
    }
}

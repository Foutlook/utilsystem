package com.foutin.juc.od;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author f2485
 * @Description
 * @date 2025/2/28 16:22
 */
public class ThreadMain {


    private static int count = 0;
    private static boolean flag = true;
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private static Semaphore semaphore1 = new Semaphore(1);
    private static Semaphore semaphore2 = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {

        synchronizedTest();



    }

    private static void synchronizedTest() {

        Object o = new Object();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                synchronized (o) {
                    if (!flag) {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("1");
                    flag = false;
                    o.notify();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                synchronized (o) {
                    if (flag) {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("2");
                    flag = true;
                    o.notify();
                }
            }
        });

        thread1.start();
        thread2.start();
    }


    private void semaphoreTest() {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                try {
                    semaphore1.acquire(); // 等待semaphore1的许可
                    System.out.print("1");
                    semaphore2.release(); // 释放semaphore2的许可
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                try {
                    semaphore2.acquire(); // 等待semaphore2的许可
                    System.out.print("2");
                    semaphore1.release(); // 释放semaphore1的许可
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
    }

}

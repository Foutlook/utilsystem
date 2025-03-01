package com.foutin.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xingkai.fan
 * @description
 * @date 2025/3/1
 */
public class ThreadMain {


    public static void main(String[] args) {
//        test2();
        test3();
    }

    // 方案 3

    private static void test3() {
        ReentrantLock lock = new ReentrantLock();
        Condition conditionX = lock.newCondition();
        Condition conditionY = lock.newCondition();
        Condition conditionZ = lock.newCondition();

        CountDownLatch countDownLatch = new CountDownLatch(3);
        AtomicInteger flag = new AtomicInteger(1);

        Thread x = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    if (flag.get() != 1) {
                        conditionX.await();
                    }
                    System.out.print("X");
                    flag.set(2);
                    conditionY.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            countDownLatch.countDown();
        });
        Thread y = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    if (flag.get() != 2) {
                        conditionY.await();
                    }
                    System.out.print("Y");
                    flag.set(3);
                    conditionZ.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            countDownLatch.countDown();
        });
        Thread z = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    if (flag.get() != 3) {
                        conditionZ.await();
                    }
                    System.out.print("Z");
                    flag.set(1);
                    conditionX.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            countDownLatch.countDown();
        });
        x.start();
        y.start();
        z.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello world");


    }





    // 方案 2
    private static void test2() {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Object o = new Object();
        AtomicInteger flag = new AtomicInteger(1);

        Thread x = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (o) {
                    if (flag.get() != 1) {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("X");
                    flag.set(2);
                    o.notifyAll();
                }
            }
            countDownLatch.countDown();
        });
        Thread y = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (o) {
                    if (flag.get() != 2) {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("Y");
                    flag.set(3);
                    o.notifyAll();
                }
            }
            countDownLatch.countDown();
        });
        Thread z = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (o) {
                    if (flag.get() != 3) {
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print("Z");
                    flag.set(1);
                    o.notifyAll();
                }
            }
            countDownLatch.countDown();
        });
        x.start();
        y.start();
        z.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello world");
    }


    // 方案 1
    public static void test1() {
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(0);
        Semaphore s3 = new Semaphore(0);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Thread a = new Thread(()-> {
            for (int i =0; i <10; i++){
                try {
                    s1.acquire();
                    System.out.print("X");
                    s2.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        });



        Thread b = new Thread(()-> {
            for (int i = 0; i <10; i++) {
                try {
                    s2.acquire();
                    System.out.print("Y");
                    s3.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        });



        Thread c = new Thread(()-> {
            for (int i = 0; i < 10; i++) {
                try {
                    s3.acquire();
                    System.out.print("Z");
                    s1.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        });

        a.start();
        b.start();
        c.start();
        try {
            long count = countDownLatch.getCount();
            System.out.println(count);

            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("hello world");
    }


}

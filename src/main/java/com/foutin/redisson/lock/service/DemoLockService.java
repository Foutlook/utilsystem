package com.foutin.redisson.lock.service;

import com.foutin.redisson.lock.cluster.annotation.CustomReentrantLock;
import com.foutin.redisson.lock.cluster.annotation.LockKey;
import org.springframework.stereotype.Service;

/**
 * 测试用的service
 *
 * @author xingkai.fan
 * @create 2019-07-27
 */
@Service
public class DemoLockService {

    @CustomReentrantLock(expireMillis = 10000)
    public void demoDiffLock(String name, @LockKey Long id) {

        System.out.println("fanxingkai-demoLockService:" + id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @CustomReentrantLock(expireMillis = 10000)
    public void demoReentrantLock(String name, @LockKey Long id) {

        System.out.println("fanxingkai-demoLockService:" + id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

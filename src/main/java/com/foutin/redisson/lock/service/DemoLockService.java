package com.foutin.redisson.lock.service;

import com.foutin.redisson.lock.cluster.DistributedLock;
import com.foutin.redisson.lock.cluster.annotation.CustomReentrantLock;
import com.foutin.redisson.lock.cluster.annotation.LockKey;
import org.redisson.RedissonMultiLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 测试用的service
 *
 * @author xingkai.fan
 * @create 2019-07-27
 */
@Service
public class DemoLockService {

    @Autowired
    private DistributedLock distributedLock;

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

    public void demoUtils(String name) {

        Boolean locked = distributedLock.tryLock(name, 2L, 10000L, TimeUnit.MILLISECONDS);
        if (locked) {
            try {
                System.out.println("fanxingkai--llll:" + name);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                distributedLock.unlock(name);
            }
        }
    }

    public void demoMultiLock(List<String> name) {
        RedissonMultiLock multiLock = distributedLock.tryMultiLock(name, 2000L, 120000L);
        try {
            System.out.println("fanxingkai--llll:" + name);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        multiLock.unlock();
    }

}

package com.foutin.zookeeper.lock.service;

import com.foutin.redisson.lock.cluster.annotation.LockKey;
import org.springframework.stereotype.Service;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/9/4 18:14
 */
@Service
public class ZKDemoLockService {

    public void demoReentrantLock() {

        System.out.println("fanxingkai-demoLockService:");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

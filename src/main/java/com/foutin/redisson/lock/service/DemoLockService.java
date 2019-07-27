package com.foutin.redisson.lock.service;

import com.foutin.redisson.lock.cluster.CustomReentrantLock;
import com.foutin.redisson.lock.cluster.LockKey;
import org.springframework.stereotype.Service;

/**
 * 测试用的service
 *
 * @author xingkai.fan
 * @create 2019-07-27
 */
@Service
public class DemoLockService {

    @CustomReentrantLock(expirationSeconds = 20)
    public void demoLock(String name, @LockKey(key = "kai") Long id) {

        System.out.println("fanxingkai-demoLockService:" + id);

    }

}

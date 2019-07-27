package com.foutin.redisson.lock.service;

import com.foutin.redisson.lock.cluster.CustomReentrantLock;
import com.foutin.redisson.lock.cluster.LockKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/24 19:10
 */
@Service
public class RedissonLockService {

    @Autowired
    private DemoLockService demoLockService;

    @CustomReentrantLock(waitTimeSeconds = 3,expirationSeconds = 5)
    public void redissonLock(@LockKey(key = "fan")String userId) {

        System.out.println("fanxingkai-redissonLockService:"+userId);

        // 引入另个类方法，同一个key
        demoLockService.demoLock("fanxingkai", 10L);

        /*throw new RuntimeException("chucuole ");*/
    }

}

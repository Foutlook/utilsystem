package com.foutin.redisson.lock.service;

import com.foutin.redisson.lock.cluster.annotation.CustomReentrantLock;
import com.foutin.redisson.lock.cluster.annotation.LockKey;
import com.foutin.redisson.lock.cluster.annotation.RetryStrategyEnum;
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

    @CustomReentrantLock(waitTimeSeconds = 8, expirationSeconds = 60)
    public void redissonLock(@LockKey(key = "fan") String userId) throws InterruptedException {

        System.out.println("fanxingkai-redissonLockService:" + userId);

        Thread.sleep(1000);
        // 1.引入另个类方法，不同的key
        /*demoLockService.demoDiffLock("fanxingkai", 10L);*/

        // 2.相同的key，重入锁
        /*demoLockService.demoReentrantLock("kaixingfan", 121212L);*/

        // 3.业务代码抛异常
        /*throw new RuntimeException("chucuole ");*/
    }

}

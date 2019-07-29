package com.foutin.redisson.lock.service;

import com.foutin.redisson.lock.cluster.CustomReentrantLock;
import com.foutin.redisson.lock.cluster.LockKey;
import com.foutin.redisson.lock.cluster.RetryStrategyEnum;
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

    @CustomReentrantLock(waitTimeSeconds = 10, expirationSeconds = 120, strategy = RetryStrategyEnum.ALL_RETRY)
    public void redissonLock(@LockKey(key = "fan")String userId) throws InterruptedException {

        System.out.println("fanxingkai-redissonLockService:"+userId);

        Thread.sleep(1000);
        // 引入另个类方法，同一个key
        /*demoLockService.demoLock("fanxingkai", 10L);*/

        /*throw new RuntimeException("chucuole ");*/
    }

}

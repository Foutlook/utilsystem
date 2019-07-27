package com.foutin.redisson.lock.cluster;

import org.springframework.stereotype.Service;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/24 19:10
 */
@Service
public class RedissonLockService {

    @CustomReentrantLock(waitTimeSeconds = 3,expirationSeconds = 5)
    public void redissonLock(@LockKey(key = "fan")String userId) {

        System.out.println("fanxingkai 3333:"+userId);
        /*throw new RuntimeException("chucuole ");*/
    }

}

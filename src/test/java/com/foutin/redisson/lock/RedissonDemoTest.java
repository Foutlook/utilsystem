package com.foutin.redisson.lock;

import com.foutin.redisson.lock.service.RedissonLockService;
import com.foutin.redisson.lock.service.ThreadUtils;
import com.foutin.utils.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * redisson lock测试
 *
 * @author xingkai.fan
 * @date 2019-07-25
 */
public class RedissonDemoTest extends BaseTest {

    @Autowired
    private RedissonLockService redissonLockService;

    /**
     * 测试同一调用链的重入锁和非重入锁
     * @throws InterruptedException
     */
    @Test
    public void testCallChain() throws InterruptedException {
        redissonLockService.redissonLock("1121892383982");
    }

    // 多线程测试
    @Test
    public void testThreadAop() throws InterruptedException {
        for (int i = 1; i < 4; i++) {
            ThreadUtils threadUtils = new ThreadUtils(redissonLockService);
            threadUtils.start();
        }
        Thread.sleep(10000);
    }

}

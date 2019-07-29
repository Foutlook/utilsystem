package com.foutin.redisson.lock;

import com.foutin.redisson.lock.service.RedissonLockService;
import com.foutin.redisson.lock.service.ThreadUtils;
import com.foutin.utils.BaseTest;
import org.junit.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.misc.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * redisson lock测试
 *
 * @author xingkai.fan
 * @create 2019-07-25
 */
public class RedissonDemoTest extends BaseTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedissonLockService redissonLockService;

    private static CountDownLatch latch = new CountDownLatch(10);

    // 尝试获取锁的最大等待时间，超过这个值，则认为获取锁失败
    private final static int waitTime = 2;
    // 锁的持有时间,超过这个时间锁会自动失效,也就是失效时间
    private final static int leaseTime = 10;

    @Test
    public void testRedisson() {

        RLock mylock = redissonClient.getLock("mylock");
        try {

            boolean tryLock = mylock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (tryLock) {
                // 成功获取锁 这里处理业务
                System.out.println("fanxignkai llllll");

                // 重入锁
                boolean lock = mylock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
                if (lock) {
                    System.out.println("fanxingkai 22222");
                }
                mylock.unlock();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            mylock.unlock();
        }
    }

    @Test
    public void testReentrantLock() throws InterruptedException {
        System.out.println(Charset.defaultCharset().name());
        System.out.println(System.getProperty("file.encoding"));
        System.out.println(URIBuilder.create("redis://192.168.6.23:6381"));
        redissonLockService.redissonLock("1121892383982");
    }

    @Test
    public void testCallChain() throws InterruptedException {
        redissonLockService.redissonLock("1121892383982");
    }

    // 多线程测试
    @Test
    public void testThreadAop() throws InterruptedException {
        for (int i = 1; i < 5; i++) {
            ThreadUtils threadUtils = new ThreadUtils(redissonLockService);
            threadUtils.start();
        }
        Thread.sleep(5000);
    }

}

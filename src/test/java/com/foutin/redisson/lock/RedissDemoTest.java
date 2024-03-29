package com.foutin.redisson.lock;

import com.foutin.rediss.lock.service.RedissLockService;
import com.foutin.redisson.lock.service.DemoLockService;
import com.foutin.redisson.lock.service.ThreadUtils;
import com.foutin.utils.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/30 14:26
 */
public class RedissDemoTest extends BaseTest {

    @Autowired
    private RedissLockService redissLockService;
    @Autowired
    private DemoLockService demoLockService;

    @Test
    public void testRedissLock() {
        redissLockService.sendRedissLock("009304930","98382938");
    }

    // 多线程测试
    @Test
    public void testThreadAop() throws InterruptedException {
        for (int i = 1; i < 4; i++) {
            ThreadUtils threadUtils = new ThreadUtils(redissLockService);
            threadUtils.start();
        }
        Thread.sleep(10000);
    }

    @Test
    public void testDemoUtils() {
        demoLockService.demoUtils("fan");
    }

    @Test
    public void testDemoMultiLock(){
        List<String> keys = new ArrayList<>();
        keys.add("fan");
        keys.add("xing");
        keys.add("kai");
        demoLockService.demoMultiLock(keys);
    };

}

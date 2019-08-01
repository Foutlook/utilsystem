package com.foutin.rediss.lock.service;

import com.foutin.rediss.lock.annotation.CustomLock;
import com.foutin.rediss.lock.annotation.LockFailAction;
import com.foutin.redisson.lock.cluster.annotation.LockKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/30 14:35
 */
@Service
public class RedissLockLineService {

    private static Logger logger = LoggerFactory.getLogger(RedissLockLineService.class);

    @CustomLock(action = LockFailAction.GIVEUP, expirationMills = 10000)
    public void testRedissLock(@LockKey String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("sendRedissLock:name:{}", name);
    }
}

package com.foutin.rediss.lock.service;

import com.foutin.rediss.lock.annotation.CustomLock;
import com.foutin.rediss.lock.annotation.LockFailAction;
import com.foutin.redisson.lock.cluster.annotation.LockKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/30 14:24
 */
@Service
public class RedissLockService {

    private static Logger logger = LoggerFactory.getLogger(RedissLockService.class);

    @Autowired
    private RedissLockLineService redissLockLineService;

    @CustomLock(action = LockFailAction.CONTINUE,expirationMills = 10000, retryTimes = 5, sleepMills = 600)
    public void sendRedissLock(@LockKey(key = "name") String name, String sex) {
        try {
            /*redissLockLineService.testRedissLock("191989");*/
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("sendRedissLock:name:{},sex:{}", name, sex);

    }
}

package com.foutin.redisson.lock.service;

import com.foutin.rediss.lock.service.RedissLockService;
import com.foutin.redisson.lock.service.RedissonLockService;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/26 14:30
 */
public class ThreadUtils extends Thread {

    private RedissonLockService redissonLockService;

    private RedissLockService redissLockService;

    public ThreadUtils(RedissonLockService redissonLockService) {
        this.redissonLockService = redissonLockService;
    }

    public ThreadUtils(RedissLockService redissLockService) {
        this.redissLockService = redissLockService;
    }


    @Override
    public void run() {
        System.out.println("run:" + Thread.currentThread().getId());
        try {
            redissonLockService.redissonLock("1092183091");
            /*redissLockService.sendRedissLock("lll","333");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

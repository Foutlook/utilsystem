package com.foutin.redisson.lock.service;

import com.foutin.redisson.lock.service.RedissonLockService;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/26 14:30
 */
public class ThreadUtils extends Thread {

    private RedissonLockService redissonLockService;

    public ThreadUtils(RedissonLockService redissonLockService) {
        this.redissonLockService = redissonLockService;
    }


    @Override
    public void run() {
        System.out.println("run:" + Thread.currentThread().getId());
        try {
            redissonLockService.redissonLock("1092183091");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

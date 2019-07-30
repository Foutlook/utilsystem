package com.foutin.redisson.lock.cluster;

import java.lang.annotation.*;

/**
 * @author xingkai.fan
 * @description TODO
 * @date 2019/7/26
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomReentrantLock {
    RetryStrategyEnum strategy() default RetryStrategyEnum.TIME_RETRY;
    /**
     * 0 不等待，大于0等待时长，小于0 一直等待
      */
    long waitTimeSeconds() default 2;
    long expirationSeconds() default 15;
}

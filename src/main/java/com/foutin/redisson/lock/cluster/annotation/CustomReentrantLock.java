package com.foutin.redisson.lock.cluster.annotation;

import java.lang.annotation.*;

/**
 * @author xingkai.fan
 * @description 自定义注解
 * @date 2019/7/26
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomReentrantLock {
    /**
     * 等待获取锁时间，等待时间内一直尝试获取锁，单位毫秒ms
     * @return long
     */
    long waitTimeMillis() default 2;

    /**
     * 锁的过期时间，单位毫秒ms
     * @return
     */
    long expireMillis() default 15;
}

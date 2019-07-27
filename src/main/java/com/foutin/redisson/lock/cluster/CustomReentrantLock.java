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
    long waitTimeSeconds() default 2;
    long expirationSeconds() default 15;
}

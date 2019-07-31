package com.foutin.rediss.lock.annotation;

import java.lang.annotation.*;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/30 13:31
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomLock {

    /** 持锁时间,单位毫秒*/
    long expirationMills() default 30000;

    /** 当获取失败时候动作*/
    LockFailAction action() default LockFailAction.CONTINUE;

    /** 重试的间隔时间,设置GIVEUP忽略此项*/
    long sleepMills() default 500;

    /** 重试次数*/
    int retryTimes() default 15;
}

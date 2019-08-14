package com.foutin.redisson.lock.cluster.annotation;

import com.foutin.redisson.lock.cluster.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/26 10:36
 */
public class LockAspectAdvice {
    private static Logger log = LoggerFactory.getLogger(LockAspectAdvice.class);

    private DistributedLock distributedLock;

    public LockAspectAdvice(DistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    public Object around(ProceedingJoinPoint point) throws Throwable{
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        CustomReentrantLock customReentrantLock = method.getAnnotation(CustomReentrantLock.class);

        // 获取参数方法注解的key
        String key = findLockKeyParameter(point);
        if (ObjectUtils.isEmpty(key)) {
            throw new RuntimeException("方法名:" + method.getName() + "参数注解key为null");
        }
        long waitTime = customReentrantLock.waitTimeMillis();
        long expiration = customReentrantLock.expireMillis();

        Object proceed;
        Boolean locked = false;
        try {
            locked = distributedLock.tryLock(key, waitTime, expiration, TimeUnit.MILLISECONDS);
            if (locked) {
                log.debug("获取锁成功,方法名：{},锁key：{}", method.getName(), key);
                // 成功获取锁 这里处理业务
                proceed = point.proceed();
            } else {
                log.warn("获取锁失败,方法名：{},锁key：{}", method.getName(), key);
                throw new RuntimeException("获取锁失败");
            }
        } catch (InterruptedException e) {
            log.warn("获取锁失败,方法名：{},锁key：{}", method.getName(), key, e);
            throw new RuntimeException("获取锁失败", e);
        } finally {
            if (locked) {
                distributedLock.unlock(key);
                log.debug("锁释放成功,方法名：{}", method.getName());
            }
        }
        return proceed;
    }


    private String findLockKeyParameter(ProceedingJoinPoint point) {
        Method m = ((MethodSignature) point.getSignature()).getMethod();
        Parameter[] parameters = m.getParameters();

        for (int i = 0, iLen = parameters.length; i < iLen; i++) {
            LockKey annotation = parameters[i].getAnnotation(LockKey.class);
            if (null == annotation) {
                continue;
            }
            if (point.getArgs()[i] == null) {
                return null;
            }
            return point.getArgs()[i].toString();
        }
        return null;
    }
}

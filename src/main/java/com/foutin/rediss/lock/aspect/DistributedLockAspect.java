package com.foutin.rediss.lock.aspect;

import com.foutin.rediss.lock.annotation.CustomLock;
import com.foutin.rediss.lock.annotation.LockFailAction;
import com.foutin.rediss.lock.impl.DistributedReentrantLock;
import com.foutin.redisson.lock.cluster.annotation.LockKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/30 13:43
 */
public class DistributedLockAspect {
    private static Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    private DistributedReentrantLock distributedReentrantLock;

    public DistributedLockAspect(DistributedReentrantLock distributedReentrantLock) {
        this.distributedReentrantLock = distributedReentrantLock;
    }

    public Object around(ProceedingJoinPoint point) throws Throwable {

        Method method = ((MethodSignature) point.getSignature()).getMethod();
        CustomLock customLock = method.getAnnotation(CustomLock.class);
        // 获取参数方法注解的key
        String key = findLockKeyParameter(point);
        if (ObjectUtils.isEmpty(key)) {
            throw new RuntimeException("方法名:" + method.getName() + "参数注解key为null");
        }
        long expirationMills = customLock.expirationMills();
        int retryTimes = customLock.action().equals(LockFailAction.CONTINUE) ? customLock.retryTimes() : 0;
        boolean lock = distributedReentrantLock.lock(key, expirationMills, retryTimes, customLock.sleepMills());
        if (!lock) {
            logger.error("获取锁失败: 方法名：{},锁key:{}", method.getName(), key);
            throw new RuntimeException("获取锁失败");
        }

        //得到锁,执行方法
        logger.info("获取锁成功: 方法名：{},锁key:{}", method.getName(), key);
        try {
            return point.proceed();
        } catch (Exception e) {
            logger.error("执行业务方法异常", e);
        } finally {
            boolean releaseResult = distributedReentrantLock.releaseLock(key);
            logger.info("释放锁{}：方法名：{},锁key：{}",releaseResult ? "success":"failed", method.getName(), key);
        }
        return null;
    }

    /**
     * 获取参数方法注解的key 这里代码重复
     *
     * @param point
     * @return
     */
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

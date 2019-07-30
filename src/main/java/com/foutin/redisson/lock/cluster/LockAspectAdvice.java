package com.foutin.redisson.lock.cluster;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/26 10:36
 */
@Component
@Aspect
public class LockAspectAdvice {

    private static Logger log = LoggerFactory.getLogger(LockAspectAdvice.class);

    @Autowired
    private RedissonClient redissonClient;

    private final static long MIX_WAIT_TIME = 0;
    private final static long DEF_WAIT_TIME = 2;
    private final static long MIX_EXPIRATION = 0;
    private final static long MAX_EXPIRATION = 3600;

    /**
     * 切点
     */
    @Pointcut("execution(* com.foutin..*(..))&&@annotation(CustomReentrantLock)")
    private void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        CustomReentrantLock customReentrantLock = method.getAnnotation(CustomReentrantLock.class);

        // 获取参数方法注解的key
        String key = findLockKeyParameter(point);
        if (key == null) {
            throw new RuntimeException("方法名:" + method.getName() + "参数注解key为null");
        }
        long waitTime = customReentrantLock.waitTimeSeconds();
        if (waitTime < MIX_WAIT_TIME) {
            waitTime = DEF_WAIT_TIME;
        }
        long expiration = customReentrantLock.expirationSeconds();
        if (expiration <= MIX_EXPIRATION) {
            expiration = MIX_EXPIRATION;
        }
        RetryStrategyEnum strategy = customReentrantLock.strategy();

        Object proceed;
        RedissonLockUtils redissonLockUtils = new RedissonLockUtils(redissonClient, key);
        Boolean locked = false;
        try {
            locked = redissonLockUtils.tryLock(waitTime, expiration);
            if (locked) {
                log.info("<<<获取锁成功,方法名：{},锁key：{}", method.getName(), key);
                // 成功获取锁 这里处理业务
                proceed = executeBusiness(point, method.getName(), key);
            } else {
                // 尝试重新获取锁
                locked = retryStrategy(redissonLockUtils, strategy);
                if (locked) {
                    log.info("重试获取锁成功,方法名：{},锁key：{},重试策略：{}", method.getName(), key, strategy);
                    // 重试锁成功，处理业务
                    proceed = executeBusiness(point, method.getName(), key);
                } else {
                    log.error("重试获取锁失败,方法名：{},锁key：{},重试策略：{}", method.getName(), key, strategy);
                    throw new RuntimeException("获取锁失败");
                }
            }
        } catch (InterruptedException e) {
            log.error("获取锁失败,方法名：{},锁key：{}", method.getName(), key, e);
            throw new RuntimeException("获取锁失败", e);
        } finally {
            if (locked) {
                redissonLockUtils.unLock();
                log.info("<<<锁释放成功,方法名：{}", method.getName());
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
            if (ObjectUtils.isEmpty(annotation.key())) {
                return null;
            }
            return annotation.key();
        }
        return null;
    }

    private Object executeBusiness(ProceedingJoinPoint point, String methodName, String key) {
        Object proceed = null;
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            log.error("业务功能执行失败,方法名：{},锁key：{}", methodName, key, e);
        }
        return proceed;
    }

    private Boolean retryStrategy(RedissonLockUtils redissonLockUtils, RetryStrategyEnum strategy) throws InterruptedException {
        return redissonLockUtils.retryLock(strategy);
    }
}

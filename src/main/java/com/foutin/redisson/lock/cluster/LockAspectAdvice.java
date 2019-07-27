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
    private final static long MAX_WAIT_TIME = 60;
    private final static long MIX_EXPIRATION = 0;
    private final static long MAX_EXPIRATION = 3600;

    // todo 锁：注解名，通用化
    @Pointcut("execution(* com.foutin..*(..))&&@annotation(CustomReentrantLock)")
    private void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        CustomReentrantLock customReentrantLock = method.getAnnotation(CustomReentrantLock.class);

        // todo key放方法入参上 参数值控制
        String key = findLockKeyParameter(point);
        log.info("ke:{}", key);
        if (key == null) {
            throw new RuntimeException("方法名:" + method.getName() + "参数注解key为null");
        }
        long waitTime = customReentrantLock.waitTimeSeconds();
        if (waitTime <= MIX_WAIT_TIME || waitTime >= MAX_WAIT_TIME) {
            throw new RuntimeException("方法名:" + method.getName() + "参数注解waitTimeSeconds设置错误(waitTimeSeconds应大于0小于60)");
        }
        long expiration = customReentrantLock.expirationSeconds();
        if (expiration <= MIX_EXPIRATION || expiration >= MAX_EXPIRATION) {
            throw new RuntimeException("方法名:" + method.getName() + "参数注解expirationSeconds设置错误(expirationSeconds应大于0小于3600)");
        }

        Object proceed = null;
        RedissonLockUtils redissonLockUtils = new RedissonLockUtils(redissonClient, key);
        try {
            Boolean tryLock = redissonLockUtils.tryLock(waitTime, expiration);
            if (tryLock) {
                log.info("<<<获取锁成功,方法名：{},锁key：{}", method.getName(), key);
                // 成功获取锁 这里处理业务
                proceed = point.proceed();
                // todo 宕机的情况下，测试
            } else {
                log.error("获取锁失败,方法名：{},锁key：{}", method.getName(), key);
                throw new RuntimeException("尝试获取锁失败");
            }
            // todo 需要处理失败
        } catch (InterruptedException e) {
            // todo error 处理有问题，获取锁、业务方法执行均会被捕获
            log.error("获取锁失败,方法名：{},锁key：{}", method.getName(), key, e);
            throw new RuntimeException("获取锁失败", e);
        } catch (Throwable e) {
            log.error("业务功能执行失败,方法名：{},锁key：{}", method.getName(), key, e);
            throw new RuntimeException("业务功能执行失败", e);
        } finally {
            redissonLockUtils.unLock();
            log.info("<<<锁释放成功,方法名：{}", method.getName());
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
}

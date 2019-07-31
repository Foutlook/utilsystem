package com.foutin.rediss.lock;

import com.foutin.rediss.lock.impl.AbstractDistributedReantrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/30 11:57
 */
@Component
public class RedisDistributedLockUtils extends AbstractDistributedReantrantLock {

    private final Logger logger = LoggerFactory.getLogger(RedisDistributedLockUtils.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private static ThreadLocal<Map<String, Long>> lockFlag = ThreadLocal.withInitial(ConcurrentHashMap::new);

    private static final String UNLOCK_LUA;

    /**
     * 当锁定资源不存在的时候才能 SET 成功
     */
    private static final String SET_IF_NOT_EXIST = "NX";
    /**
     * 锁定的资源的自动过期时间，单位是毫秒
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    static {
        UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
                "then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end ";
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        final long threadId = Thread.currentThread().getId();
        logger.info("获取锁开始...");
        boolean result = setRedisLock(key, threadId, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes-- > 0) {
            try {
                logger.info("获取锁失败, 开始重试,剩余重试次数：{}", retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setRedisLock(key, threadId, expire);
        }
        return result;
    }

    private boolean setRedisLock(String key, long threadId, long expire) {
        try {
            String result = (String) redisTemplate.execute((RedisCallback<String>) redisConnection -> {
                JedisCommands commands = (JedisCommands) redisConnection.getNativeConnection();
                Map<String, Long> valueCountMap = lockFlag.get();
                String lockValue = generaterLockValue(threadId);
                // 情况一：为空
                if (ObjectUtils.isEmpty(valueCountMap)) {
                    String set = commands.set(key, lockValue, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expire);
                    logger.info("lockFlag为空,set结果：{}, key:{},value:{}", set, key, lockValue);
                    // 这里不为空，说明拿到锁
                    if (!StringUtils.isEmpty(set)) {
                        valueCountMap.put(lockValue, 1L);
                        lockFlag.set(valueCountMap);
                    }
                    return set;
                }

                String oldLockValue = commands.get(key);
                // 情况二：key值过期或不存在，重新设置value
                if (oldLockValue == null) {
                    String set = commands.set(key, lockValue, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expire);
                    if (!StringUtils.isEmpty(set)) {
                        logger.info("key值过期或不存在,set结果：{}, key:{},value:{}", set, key, lockValue);
                        valueCountMap.put(lockValue, 1L);
                        lockFlag.set(valueCountMap);
                    }
                    return set;
                }
                // 情况三: 不为空，并存在相同的值
                String sameValue = valueCountMap.keySet().stream().filter(oldLockValue::equals).collect(Collectors.toList()).get(0);
                if (!StringUtils.isEmpty(sameValue)) {
                    logger.info("不为空，并存在相同的值(重入),key:{},value:{}", key, lockValue);
                    // 重入，count值加1
                    Long count = valueCountMap.get(sameValue);
                    count++;
                    valueCountMap.put(sameValue, count);
                    lockFlag.set(valueCountMap);
                    return sameValue;
                }
                return null;
            });
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            logger.error("加锁失败", e);
        }
        return false;
    }

    @Override
    public boolean releaseLock(String key) {
        logger.info("解锁开始...");
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();

            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            Long result = (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    JedisCommands commands = (JedisCommands) nativeConnection;
                    String oldLockValue = commands.get(key);
                    Map<String, Long> valueCountMap = lockFlag.get();
                    if (StringUtils.isEmpty(oldLockValue) || ObjectUtils.isEmpty(valueCountMap)) {
                        logger.info("lockFlog为空或redis中不存在值");
                        return 0L;
                    }
                    String sameValue = valueCountMap.keySet().stream().filter(oldLockValue::equals).collect(Collectors.toList()).get(0);
                    if (StringUtils.isEmpty(sameValue)) {
                        logger.info("本地lockFlog中不存在相同的值");
                        return 0L;
                    }
                    Long count = valueCountMap.get(sameValue);

                    // 存在相同的值
                    if (count == 1) {
                        args.add(sameValue);
                        Long resultEval = (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                        valueCountMap.remove(sameValue);
                        lockFlag.set(valueCountMap);
                        logger.info("解锁：key：{}， value:{}，count：{}", key, sameValue, count);
                        return resultEval;
                    }
                    logger.info("重入解锁：key：{}， value:{}，count：{}", key, sameValue, count);
                    count--;
                    valueCountMap.put(sameValue, count);
                    lockFlag.set(valueCountMap);
                    return 1L;
                }
                return 0L;
            });

            if (result != null && result > 0) {
                lockFlag.remove();
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.error("释放锁方法异常", e);
        }
        return false;
    }

    private String generaterLockValue(long threadId) {
        String macAddress = PlatformUtils.macAddress();
        int jvmPid = PlatformUtils.jvmPid();
        return macAddress + "_" + jvmPid + "_" + threadId;
    }

}

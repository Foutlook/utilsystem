package com.foutin.mysql.lock;

/**
 * 获取锁名称/时间相关信息
 *
 * @Author XingKong
 * @Date 2023/6/7 14:01
 */
public interface IDistributedLockModule {

    /**
     * 获取锁名称
     *
     * @return 锁名称
     */
    String getLockName();

    /**
     * 获取锁名称 使用前缀+key作为锁名称
     *
     * @param key 锁前缀
     * @return 锁名称
     */
    default String getLockName(String key) {
        return getLockName() + DistributedLockConstants.DIFF_SEPARATOR + key;
    }

    /**
     * 获取轮询抢占锁间隔时间
     *
     * @return 轮询抢占锁间隔时间
     */
    int getPollGap();

    /**
     * 获取锁过期时间
     *
     * @return 锁过期时间
     */
    int getExpireInterval();

    /**
     * 获取锁清除时间
     *
     * @return 锁清除时间
     */
    int getEvictInterval();

    /**
     * 获取锁等待时间
     *
     * @return 锁等待时间
     */
    long getWaitTime();
}

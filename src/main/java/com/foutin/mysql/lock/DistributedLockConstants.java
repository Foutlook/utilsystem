package com.foutin.mysql.lock;

/**
 * @author f2485
 * @Description
 * @date 2023/6/27 14:33
 */
public final class DistributedLockConstants {

    public static final String DIFF_SEPARATOR = "_";
    /**
     * 默认轮询抢占锁间隔时间（单位：毫秒）
     */
    public static final int DEFAULT_POLL_GAP = 10;

    /**
     * 默认锁过期时间，-1 表示会定时续期（单位：毫秒）
     */
    public static final int DEFAULT_EXPIRE_INTERVAL = -1;
    /**
     * 默认锁清除时间，-1 表示永不清除（单位：毫秒）
     */
    public static final int DEFAULT_EVICT_INTERVAL = 6 * 3600 * 1000;
    /**
     * 默认锁等待时间，-1 表示永久等待（单位：毫秒）
     */
    public static final long DEFAULT_WAIT_TIME = -1;

    private DistributedLockConstants() {
        throw new IllegalStateException("Utility class");
    }

}

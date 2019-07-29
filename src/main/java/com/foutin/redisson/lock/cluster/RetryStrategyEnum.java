package com.foutin.redisson.lock.cluster;

/**
 * @author xingkai.fan
 * @description TODO
 * @date 2019/7/29
 */
public enum RetryStrategyEnum {
    /**
     * 不重试,retryTime为0
     */
    NO_RETRY(0),
    /**
     * 重试数次，retryTime为一个正整数值
     */
    TIME_RETRY(3),
    /**
     * 无限次重试，直到获取锁 retryTime为负数
     */
    ALL_RETRY(-1);

    private Integer time;

    RetryStrategyEnum(Integer time) {
        setTime(time);
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public static RetryStrategyEnum setEnum(Integer time, Integer newTime) {
        if (time == null) {
            return null;
        }
        for (RetryStrategyEnum retryStrategyEnum : RetryStrategyEnum.values()) {
            if (retryStrategyEnum.getTime().equals(time)) {
                retryStrategyEnum.setTime(newTime);
                return retryStrategyEnum;
            }
        }
        return null;
    }

}

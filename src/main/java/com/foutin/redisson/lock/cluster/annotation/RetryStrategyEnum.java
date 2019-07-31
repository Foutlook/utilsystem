package com.foutin.redisson.lock.cluster.annotation;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/29
 */
public enum RetryStrategyEnum {
    /**
     * 不重试
     */
    GIVEUP_RETRY("不重试,未获取到锁直接报错"),
    /**
     * 在等待时间里重试数次
     */
    CONTINUE_RETRY("重试指定等待时间,获取锁失败报错");

    private String retryValue;

    RetryStrategyEnum(String retryValue) {
        setRetryValue(retryValue);
    }

    public String getRetryValue() {
        return retryValue;
    }

    public void setRetryValue(String retryValue) {
        this.retryValue = retryValue;
    }

    public static RetryStrategyEnum getEnum(String retryValue) {
        if (retryValue == null) {
            return null;
        }
        for (RetryStrategyEnum retryStrategyEnum : RetryStrategyEnum.values()) {
            if (retryStrategyEnum.getRetryValue().equals(retryValue)) {
                return retryStrategyEnum;
            }
        }
        return null;
    }

}

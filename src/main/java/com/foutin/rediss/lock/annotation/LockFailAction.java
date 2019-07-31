package com.foutin.rediss.lock.annotation;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/7/30
 */
public enum LockFailAction {
    /**
     * 放弃
     */
    GIVEUP,
    /**
     * 继续
     */
    CONTINUE
}

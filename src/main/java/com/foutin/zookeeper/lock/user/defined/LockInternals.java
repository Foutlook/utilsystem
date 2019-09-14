package com.foutin.zookeeper.lock.user.defined;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @date 2019-09-14
 */
public interface LockInternals {

    String attemptLock(long time, TimeUnit unit, byte[] lockNodeBytes) throws Exception;

    void setPath(String path);


}

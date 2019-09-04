package com.foutin.zookeeper.lock;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author xingkai.fan
 * @description 读取配置文件
 * @date 2019/9/4 15:56
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "spring-config.properties")
public class ZookeeperConfig {

    private String zkServerLists;

    private String zkRetryPolicySleepTimeMs;

    private String zkMaxRestries;

    private String zkReentrantLockPath;

    private String zkReadWriteLockPath;

    private String zkSharedMultiLock;

    @DisconfFileItem(name = "zk.serverLists", associateField = "zkServerLists")
    public String getZkServerLists() {
        return zkServerLists;
    }

    public void setZkServerLists(String zkServerLists) {
        this.zkServerLists = zkServerLists;
    }

    @DisconfFileItem(name = "zk.retryPolicy.sleepTimeMs", associateField = "zkRetryPolicySleepTimeMs")
    public String getZkRetryPolicySleepTimeMs() {
        return zkRetryPolicySleepTimeMs;
    }

    public void setZkRetryPolicySleepTimeMs(String zkRetryPolicySleepTimeMs) {
        this.zkRetryPolicySleepTimeMs = zkRetryPolicySleepTimeMs;
    }

    @DisconfFileItem(name = "zk.maxRestries", associateField = "zkMaxRestries")
    public String getZkMaxRestries() {
        return zkMaxRestries;
    }

    public void setZkMaxRestries(String zkMaxRestries) {
        this.zkMaxRestries = zkMaxRestries;
    }

    @DisconfFileItem(name = "zk.sharedReentrantLock", associateField = "zkReentrantLockPath")
    public String getZkReentrantLockPath() {
        return zkReentrantLockPath;
    }

    public void setZkReentrantLockPath(String zkReentrantLockPath) {
        this.zkReentrantLockPath = zkReentrantLockPath;
    }

    @DisconfFileItem(name = "zk.sharedReadWriteLock", associateField = "zkReadWriteLockPath")
    public String getZkReadWriteLockPath() {
        return zkReadWriteLockPath;
    }

    public void setZkReadWriteLockPath(String zkReadWriteLockPath) {
        this.zkReadWriteLockPath = zkReadWriteLockPath;
    }

    @DisconfFileItem(name = "zk.sharedMultiLock", associateField = "zkSharedMultiLock")
    public String getZkSharedMultiLock() {
        return zkSharedMultiLock;
    }

    public void setZkSharedMultiLock(String zkSharedMultiLock) {
        this.zkSharedMultiLock = zkSharedMultiLock;
    }
}

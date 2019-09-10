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

    private String zkNameSpace;

    private String zkReentrantLockPath;

    private String zkReadWriteLockPath;

    private String zkSharedMultiLock;

    @DisconfFileItem(name = "zk.nameSpace", associateField = "zkNameSpace")
    public String getZkNameSpace() {
        return zkNameSpace;
    }

    public void setZkNameSpace(String zkNameSpace) {
        this.zkNameSpace = zkNameSpace;
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

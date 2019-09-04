package com.foutin.zk.lock;

import com.foutin.utils.BaseTest;
import com.foutin.zookeeper.lock.interprocessmutex.DistributedZkLock;
import com.foutin.zookeeper.lock.service.ZKDemoLockService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xingkai.fan
 * @description
 * @date 2019/9/4 17:48
 */
public class ZKDemoTest extends BaseTest {

    @Autowired
    private ZKDemoLockService zkDemoLockService;
    @Autowired
    private DistributedZkLock distributedZkLock;

    @Test
    public void zookeeperService() {
        distributedZkLock.sharedReentrantLock(1000L);
        zkDemoLockService.demoReentrantLock();
    }

}

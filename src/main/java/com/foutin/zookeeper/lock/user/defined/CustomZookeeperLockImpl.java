package com.foutin.zookeeper.lock.user.defined;

import com.foutin.zookeeper.lock.client.ZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xingkai.fan
 * @date 2019-09-11
 */
public class CustomZookeeperLockImpl implements CustomZookeeperLock {

    @Autowired
    private ZookeeperClient zookeeperClient;

    @Override
    public void createNote(String path) throws Exception {
        CuratorFramework curatorFramework = zookeeperClient.getCuratorFramework();
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(path);
    }
}

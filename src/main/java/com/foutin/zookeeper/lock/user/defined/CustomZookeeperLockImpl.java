package com.foutin.zookeeper.lock.user.defined;

import com.foutin.zookeeper.lock.client.ZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @date 2019-09-11
 */
public class CustomZookeeperLockImpl implements CustomZookeeperLock {
    private static final Logger logger = LoggerFactory.getLogger(CustomZookeeperLockImpl.class);

    @Autowired
    private ZookeeperClient zookeeperClient;

    @Override
    public void createNote(String path) throws Exception {
        CuratorFramework curatorFramework = zookeeperClient.getCuratorFramework();
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (!ObjectUtils.isEmpty(stat)) {
            // 添加监控
            lockNodeWatch(path);
        }
        // 没有节点，直接创建
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(path);
    }

    @Override
    public NodeCache lockNodeWatch(String path) {
        CuratorFramework curatorFramework = zookeeperClient.getCuratorFramework();
        final NodeCache cache = new NodeCache(curatorFramework, path, true);
        ExecutorService executor = new ThreadPoolExecutor(1,10,60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5),new ThreadPoolExecutor.AbortPolicy());
        cache.getListenable().addListener(() -> {
            Stat stat = curatorFramework.checkExists().forPath(path);
            if (ObjectUtils.isEmpty(stat)) {
                curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(path);
                cache.close();
            }
        },executor);

        try {
            cache.start();
        } catch (Exception e) {
            logger.error("添加锁监控失败", e);
            throw new RuntimeException(e);
        }
        return cache;
    }

    @Override
    public void closeNodeWatch(NodeCache cache) {
        if (ObjectUtils.isEmpty(cache)) {
            return;
        }
        try {
            cache.close();
        } catch (IOException e) {
            logger.warn("关闭锁监控失败", e);
            throw new RuntimeException(e);
        }
    }
}

package com.foutin.zk.use;

import com.foutin.zookeeper.use.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @author f2485
 * @Description 客户端监听实战
 * @date 2025/1/14 14:16
 */
public class ZkWatcherDemo {

    private static final Logger log = LoggerFactory.getLogger(ZkWatcherDemo.class);
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private String workerPath = "/test/listener/remoteNode";
    private String subWorkerPath = "/test/listener/remoteNode/id-";


    /**
     * 利用 Watcher 来对节点进行监听操作
     */
    @Test
    public void testWatcher() {
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        // CuratorFramework client = ZKclient.instance.getClient();
        // boolean isExist = ZKclient.instance.isNodeExist(workerPath);
        // if (!isExist) {
        //     ZKclient.instance.createNode(workerPath, null);
        // }
        try {
            // 检查节点是否存在，没有则创建
            client.start();
            Stat stat = client.checkExists().forPath(workerPath);
            if (null == stat) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(workerPath);
            }

            Watcher w = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("监听到的变化 watchedEvent = " +
                            watchedEvent);
                }
            };
            byte[] content = client.getData()
                    .usingWatcher(w).forPath(workerPath);
            log.info("监听节点内容：" + new String(content));
            // 第一次变更节点数据
            client.setData().forPath(workerPath,
                    "第 1 次更改内容".getBytes());
            // 第二次变更节点数据
            client.setData().forPath(workerPath,
                    "第 2 次更改内容".getBytes());
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


    /**
     * NodeCache 节点缓存的监听
     * 使用NodeCache来监听节点的事件
     */
    @Test
    public void testNodeCache() {
        // 检查节点是否存在，没有则创建
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            // 检查节点是否存在，没有则创建
            client.start();
            Stat stat = client.checkExists().forPath(workerPath);
            if (null == stat) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(workerPath);
            }
            NodeCache nodeCache = new NodeCache(client, workerPath, false);
            NodeCacheListener listener = new NodeCacheListener() {
                @Override
                public void nodeChanged() throws UnsupportedEncodingException {
                    ChildData childData = nodeCache.getCurrentData();
                    log.info("ZNode 节点状态改变, path={}",
                            childData.getPath());
                    log.info("ZNode 节点状态改变, data={}",
                            new String(childData.getData(), "Utf-8"));
                    log.info("ZNode 节点状态改变, stat={}",
                            childData.getStat());
                }
            };
            // 启动节点的事件监听
            nodeCache.getListenable().addListener(listener);
            nodeCache.start();
            // 第 1 次变更节点数据
            client.setData().forPath(workerPath,
                    "第 1 次更改内容".getBytes());
            Thread.sleep(1000);
            // 第 2 次变更节点数据
            client.setData().forPath(workerPath,
                    "第 2 次更改内容".getBytes());
            Thread.sleep(1000);
            // 第 3 次变更节点数据
            client.setData().forPath(workerPath,
                    "第 3 次更改内容".getBytes());
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("创建 NodeCache 监听失败, path={}", workerPath);
        }
    }
}

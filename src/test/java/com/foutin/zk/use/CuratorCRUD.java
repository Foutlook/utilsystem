package com.foutin.zk.use;

import com.foutin.zookeeper.use.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author f2485
 * @Description
 * @date 2025/1/14 10:11
 */
public class CuratorCRUD {

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    /**
     * 创建节点
     */
    @Test
    public void createNode() {
        // 客户端实例
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            // 启动客户端实例,连接服务器
            client.start();
            // 创建一个 ZNode 节点
            // 节点的数据为 payload
            String data = "hello";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/CRUD/node-1";
            client.create()
                    .creatingParentsIfNeeded()
                    // ZooKeeper节点有四种类型：
                    // （1）PERSISTENT 持久化节点
                    // （2）PERSISTENT_SEQUENTIAL 持久化顺序节点
                    // （3）PHEMERAL 临时节
                    // （4）EPHEMERAL_SEQUENTIAL 临时顺序节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


    /**
     * 读取节点
     */
    @Test
    public void readNode() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            // 启动客户端实例,连接服务器
            client.start();
            String zkPath = "/test/CRUD/node-1";
            Stat stat = client.checkExists().forPath(zkPath);
            if (null != stat) {
                // 读取节点的数据
                byte[] payload = client.getData().forPath(zkPath);
                String data = new String(payload, "UTF-8");
                System.out.println("read data:" + data);
                String parentPath = "/test";
                List<String> children =
                        client.getChildren().forPath(parentPath);
                for (String child : children) {
                    System.out.println("child:" + child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 同步更新节点
     */
    @Test
    public void updateNode() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            // 启动客户端实例,连接服务器
            client.start();
            String data = "hello world";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/CRUD/node-1";
            client.setData()
                    .forPath(zkPath, payload);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 更新节点 - 异步模式
     */
    @Test
    public void updateNodeAsync() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            // 启动客户端实例,连接服务器
            client.start();
            String data = "hello ,every body! ";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/CRUD/remoteNode-1";
            CountDownLatch latch = new CountDownLatch(1); // 创建一个CountDownLatch实例
            client.setData()
                    .inBackground(new BackgroundCallback() {
                        @Override
                        public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                            System.out.println("update node Async");
                            latch.countDown(); // 通知等待的线程操作已完成
                        }
                    }) // 设置回调实例
                    .forPath(zkPath, payload);
            latch.await(); // 等待异步操作完成
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


    //.......省略其他
    @Test
    public void deleteNode() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            // 启动客户端实例,连接服务器
            client.start();
            // 删除节点
            String zkPath = "/test/CRUD/node-1";
            client.delete().forPath(zkPath);
            // 删除后查看结果
            String parentPath = "/test";
            List<String> children =
                    client.getChildren().forPath(parentPath);
            for (String child : children) {
                System.out.println("child:" + child);
            }
            readNode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}

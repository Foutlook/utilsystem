package com.foutin.zk.use;

import com.foutin.zookeeper.use.ClientFactory;
import com.foutin.zookeeper.use.IDMaker;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

/**
 * @author f2485
 * @Description
 * @date 2025/1/14 11:21
 */
public class IDMakerTester {

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    @Test
    public void testMakeId() {
        IDMaker idMaker = new IDMaker();
        String nodeName = "/test/IDMaker/ID-";
        for (int i = 0; i < 10; i++) {
            String id = idMaker.makeId(nodeName);
            System.out.println("第" + i + "个创建的 id 为:" + id);
        }
        readNode();
        idMaker.destroy();
    }

    public void readNode() {
        // 创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            // 启动客户端实例,连接服务器
            client.start();
            String zkPath = "/test/IDMaker";
            Stat stat = client.checkExists().forPath(zkPath);
            if (null != stat) {
                // 读取节点的数据
                byte[] payload = client.getData().forPath(zkPath);
                String data = new String(payload, "UTF-8");
                System.out.println("read data:" + data);
                String parentPath = "/test/IDMaker";
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
}

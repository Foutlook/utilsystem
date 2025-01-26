package com.foutin.zookeeper.use;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author f2485
 * @Description 分布式ID生成器
 * @date 2025/1/14 11:17
 */
public class IDMaker {

    private static final Logger logger = LoggerFactory.getLogger(IDMaker.class);
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final int BASE_SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 3;

    private CuratorFramework client;

    public IDMaker() {
        this.client = ClientFactory.createWithOptions(ZK_ADDRESS,
                new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES),
                10000, 5000);
        this.client.start();
    }

    /**
     * 创建临时顺序节点
     *
     * @param pathPrefix 节点路径前缀
     * @return 创建后的完整路径名称
     */
    private String createSeqNode(String pathPrefix) {
        try {
            // 创建一个 ZNode 顺序节点
            // 为了避免 zookeeper 的顺序节点暴增，建议创建后，直接删除创建的节点
            return client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(pathPrefix);
        } catch (Exception e) {
            logger.error("Failed to create sequential node", e);
        }
        return null;
    }

    // 获取 ID 值
    public String makeId(String nodeName) {
        String str = createSeqNode(nodeName);
        if (null == str) {
            return null;
        }
        // 取得 ZK 节点的末尾序号
        int index = str.lastIndexOf(nodeName);
        if (index >= 0) {
            index += nodeName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    public void destroy() {
        CloseableUtils.closeQuietly(client);
    }
}
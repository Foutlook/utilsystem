package com.foutin.zookeeper.use;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author f2485
 * @Description  使用Curator框架创建客户端
 * @date 2025/1/13 17:21
 */
public class ClientFactory {


    /**
     * 方式一
     *
     * @param connectionString zk 的连接地址
     * @return CuratorFramework 实例
     */
    public static CuratorFramework createSimple(String connectionString) {
        // 重试策略:第一次重试等待 1s，第二次重试等待 2s，第三次重试等待 4s
        // 第一个参数：等待时间的基础单位，单位为毫秒
        // 第二个参数：最大重试次数
        ExponentialBackoffRetry retryPolicy =
                new ExponentialBackoffRetry(1000, 3);
        // 使用工厂类 CuratorFrameworkFactory 的静态 newClient（…）方法
        // 第一个参数：zk 的连接地址
        // 第二个参数：重试策略
        return CuratorFrameworkFactory.newClient(
                connectionString, retryPolicy);
    }

    /**
     * 方式二
     *
     * @param connectionString    zk 的连接地址
     * @param retryPolicy         重试策略
     * @param connectionTimeoutMs 连接超时时间
     * @param sessionTimeoutMs    会话超时时间
     * @return CuratorFramework 实例
     */
    public static CuratorFramework createWithOptions(String connectionString,
                                                     RetryPolicy retryPolicy,
                                                     int connectionTimeoutMs,
                                                     int sessionTimeoutMs) {
        // 使用工厂类 CuratorFrameworkFactory 的静态 builder 构造者方法
        // builder 模式创建 CuratorFramework 实例
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                // 其他的创建选项
                .build();
    }
}

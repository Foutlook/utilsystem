package com.foutin.zookeeper.lock.user.defined;

import org.apache.curator.RetryLoop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author xingkai.fan
 * @date 2019-09-13
 */
@Service
public class LockInternalsImpl implements LockInternals{

    @Autowired
    private ZookeeperLockManager zookeeperLockManager;

    private String path;


    /**
     * 尝试获取锁，并返回锁对应的Zookeeper临时顺序节点的路径
     * @param time
     * @param unit
     * @param lockNodeBytes
     * @return
     */
    @Override
    public String attemptLock(long time, TimeUnit unit, byte[] lockNodeBytes) throws Exception {
        final long startMillis = System.currentTimeMillis();
        // 无限等待时，millisToWait为null
        final Long millisToWait = (unit != null) ? unit.toMillis(time) : null;
        // 创建ZNode节点时的数据内容，无关紧要，这里为null，采用默认值（IP地址）
        final byte[] localLockNodeBytes = (lockNodeBytes != null) ? new byte[0] : lockNodeBytes;
        // 当前已经重试次数，与CuratorFramework的重试策略有关
        int retryCount = 0;

        // 在Zookeeper中创建的临时顺序节点的路径，相当于一把待激活的分布式锁
        // 激活条件：同级目录子节点，名称排序最小（排队，公平锁），后续继续分析
        String ourPath = null;
        // 是否已经持有分布式锁
        boolean hasTheLock = false;
        // 是否已经完成尝试获取分布式锁的操作
        boolean isDone = false;

        while (!isDone) {
            isDone = true;
            try {
                // 从InterProcessMutex的构造函数可知实际driver为StandardLockInternalsDriver的实例
                // 在Zookeeper中创建临时顺序节点
                ourPath = zookeeperLockManager.createNote(path, localLockNodeBytes);
                if (ourPath != null) {
                    hasTheLock = true;
                }
                // 循环等待来激活分布式锁，实现锁的公平性，后续继续分析
//                hasTheLock = internalLockLoop(startMillis, millisToWait, ourPath);
            } catch (Exception e) {
                // 容错处理，不影响主逻辑的理解，可跳过
                // 因为会话过期等原因，StandardLockInternalsDriver因为无法找到创建的临时顺序节点而抛出NoNodeException异常
                if (zookeeperLockManager.getCuratorFramework().getZookeeperClient().getRetryPolicy().allowRetry(retryCount++,
                        System.currentTimeMillis() - startMillis, RetryLoop.getDefaultRetrySleeper())) {
                    // 满足重试策略尝试重新获取锁
                    isDone = false;
                } else {
                    // 不满足重试策略则继续抛出NoNodeException
                    throw e;
                }
            }
        }
        if (hasTheLock) {
            // 成功获得分布式锁，返回临时顺序节点的路径，上层将其封装成锁信息记录在映射表，方便锁重入
            return ourPath;
        }
        // 获取分布式锁失败，返回null
        return null;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }
}

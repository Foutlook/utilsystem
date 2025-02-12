package com.foutin.juc.lock;

import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author f2485
 * @Description CLH 锁的简单实现版本
 * CLH 锁其实就是一种是基于队列（具体为单向链表）排队的一种自旋锁，由于是 Craig、Landin
 * 和 Hagersten 三人一起发明的，因此被命名为 CLH 锁，也叫 CLH 队列锁。
 * 简单的 CLH 锁可以基于单向链表实现，申请加锁的线程首先会通过 CAS 操作在单向链表的
 * 尾部增加一个节点，之后，该线程只需要在其前驱节点上进行普通自旋，等待前驱节点释放锁即
 * 可。由于 CLH 锁只有在节点入队时进行一下 CAS 的操作，在节点在加入队列之后，抢锁线程不
 * 需要进行 CAS 自旋，只需普通自旋即可。所以，在争用激烈的场景下，CLH 锁能大大减少的 CAS
 * 操作的数量，以避免 CPU 的总线风暴。
 *
 * JUC 中显式锁基于 AQS 抽象队列同步器，而 AQS 是 CLH 锁的一个变种，为了方便大
 * 家理解 AQS 原理（此为 Java 工程师的必备知识），这里详细介绍一下 CLH 锁的实现
 * 和核心原理。
 * @date 2025/2/12 17:07
 */
public class CLHLock implements Lock {


    /**
     * 当前节点的线程本地变量
     */
    private static ThreadLocal<Node> curNodeLocal = new ThreadLocal<>();
    /**
     * CLHLock 队列的尾部指针，使用 AtomicReference，方便进行 CAS 操作
     */
    private AtomicReference<Node> tail = new AtomicReference<>(null);
    public CLHLock()
    {
        //设置尾部节点
        tail.getAndSet(Node.EMPTY);
    }


    @Override
    public void lock() {
        Node curNode = new Node(true, null);
        Node preNode = tail.get();
        //CAS 自旋：将当前节点插入到队列的尾部
        while (!tail.compareAndSet(preNode, curNode))
        {
            preNode = tail.get();
        }
        //设置前驱
        curNode.setPrevNode(preNode);
        // 自旋，监听前驱节点的 locked 变量，直到其值为 false
        // 若前继节点的 locked 状态为 true，则表示前一线程还在抢占或者占有锁
        while (curNode.getPrevNode().isLocked())
        {
            //让出 CPU 时间片，提高性能
            Thread.yield();
        }
        // 能执行到这里，说明当前线程获取到了锁
        // Print.tcfo("获取到了锁！！！");
        //将当前节点缓存在线程本地变量中，释放锁会用到
        curNodeLocal.set(curNode);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        Node curNode = curNodeLocal.get();
        curNode.setLocked(false);
        curNode.setPrevNode(null);//help for GC
        curNodeLocal.set(null); //方便下一次抢锁
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Data
    static class Node
    {
        public Node(boolean locked, Node prevNode)
        {
            this.locked = locked;
            this.prevNode = prevNode;
        }
        //true：当前线程正在抢占锁、或者已经占有锁
        // false：当前线程已经释放锁，下一个线程可以占有锁了
        volatile boolean locked;
        //前一个节点，需要监听其 locked 字段
        Node prevNode;
        //空节点
        public static final Node EMPTY = new Node(false, null);
    }
}

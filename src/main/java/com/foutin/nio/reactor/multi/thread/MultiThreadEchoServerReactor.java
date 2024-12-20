package com.foutin.nio.reactor.multi.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author f2485
 * @Description
 * @date 2024/12/13 11:34
 */
public class MultiThreadEchoServerReactor {

    AtomicInteger next = new AtomicInteger(0);
    // selectors 集合,引入多个 selector 选择器
    Selector[] workSelectors = new Selector[2];
    Selector bossSelector;
    // 引入多个子反应器
    Reactor[] workReactors = null;
    Reactor bossReactor = null;

    ServerSocketChannel serverSocket;

    /**
     * 下面的代码创建了三个子反应器，一个bossReactor负责新连接事件的反应处理（查
     * 询、分发、处理），bossReactor和boss选择器进行绑定；两个workReactor负责普通IO事件
     * 的查询和分发，分别绑定一个worker选择器。
     * 服务端的监听通道注册到boss选择器，而所有的Socket传输通道通过轮询策略注册到
     * worke选择器，从而实现了新连接监听和IO读写事件监听的线程分离。
     * @throws IOException
     */
    MultiThreadEchoServerReactor() throws IOException {
        // 初始化多个 selector 选择器
        bossSelector = Selector.open();// 用于监听新连接事件
        workSelectors[0] = Selector.open(); // 用于监听 read、write 事件
        workSelectors[1] = Selector.open(); // 用于监听 read、write 事件
        serverSocket = ServerSocketChannel.open();
        InetSocketAddress address =
                new InetSocketAddress("SOCKET_SERVER_IP", 10000);
        serverSocket.socket().bind(address);
        serverSocket.configureBlocking(false);// 非阻塞
        // bossSelector,负责监控新连接事件, 将 serverSocket 注册到 bossSelector
        SelectionKey sk = serverSocket.register(
                bossSelector, SelectionKey.OP_ACCEPT);
        // 绑定 Handler：新连接监控 handler 绑定到 SelectionKey（选择键）
        sk.attach(new AcceptorHandler());
        // bossReactor 反应器，处理新连接的 bossSelector
        bossReactor = new Reactor(bossSelector);
        // 第一个子反应器，一子反应器负责一个 worker 选择器
        Reactor workReactor1 = new Reactor(workSelectors[0]);
        // 第二个子反应器，一子反应器负责一个 worker 选择器
        Reactor workReactor2 = new Reactor(workSelectors[1]);
        workReactors = new Reactor[]{workReactor1, workReactor2};
    }

    // 反应器
    class Reactor implements Runnable {
        // 每条线程负责一个选择器的查询
        final Selector selector;

        public Reactor(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // 单位为毫秒
                    selector.select(1000);
                    Set<SelectionKey> selectedKeys =
                            selector.selectedKeys();
                    if (null == selectedKeys || selectedKeys.size() == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> it = selectedKeys.iterator();
                    while (it.hasNext()) {
                        // Reactor 负责 dispatch 收到的事件
                        SelectionKey sk = it.next();
                        dispatch(sk);
                    }
                    selectedKeys.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        void dispatch(SelectionKey sk) {
            Runnable handler = (Runnable) sk.attachment();
            // 调用之前 attach 绑定到选择键的 handler 处理器对象
            if (handler != null) {
                handler.run();
            }
        }
    }

    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                System.out.println("接收到一个新的连接");
                if (channel != null) {
                    int index = next.get();
                    System.out.println("选择器的编号：" + index);
                    Selector selector = workSelectors[index];
                    new MultiThreadEchoHandler(selector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (next.incrementAndGet() == workSelectors.length) {
                next.set(0);
            }
        }
    }


    private void startService() {
        // 一子反应器对应一条线程
        new Thread(bossReactor).start();
        new Thread(workReactors[0]).start();
        new Thread(workReactors[1]).start();
    }
}

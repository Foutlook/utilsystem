package com.foutin.nio.netty.simple.cray.im.cocurrent;


import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:34
 */
public class FutureTaskScheduler {

    // 方法二是使用自建的线程池时，专用于处理耗时操作
    static ThreadPoolExecutor mixPool = null;

    static {
        mixPool = ThreadUtil.getMixedTargetThreadPool();
    }

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler() {

    }

    /**
     * 添加任务
     *
     * @param executeTask
     */


    public static void add(ExecuteTask executeTask) {
        mixPool.submit(() -> {
            executeTask.execute();
        });
    }

}

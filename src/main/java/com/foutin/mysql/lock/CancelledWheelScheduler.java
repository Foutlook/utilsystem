package com.foutin.mysql.lock;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author XingKong
 * @Date 2023/6/12 10:16
 */
public class CancelledWheelScheduler {

    private final ConcurrentMap<String, Timeout> cancelledScheduledFutures = PlatformDependent.newConcurrentHashMap();
    private final HashedWheelTimer executorService;
    private Executor taskExecutor;

    public CancelledWheelScheduler() {
        this.executorService = new HashedWheelTimer();
    }

    public CancelledWheelScheduler(ThreadFactory threadFactory) {
        this.executorService = new HashedWheelTimer(threadFactory);
    }

    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void cancel(String key) {
        Timeout timeout = this.cancelledScheduledFutures.remove(key);
        if (timeout != null) {
            timeout.cancel();
        }
    }

    public boolean contains(String key) {
        return this.cancelledScheduledFutures.containsKey(key);
    }

    public void schedule(final Runnable runnable, long delay, TimeUnit unit) {
        this.executorService.newTimeout(timeout ->
                CancelledWheelScheduler.this.taskExecutor.execute(runnable), delay, unit);
    }

    public void schedule(final String key, final Runnable runnable, long delay, TimeUnit unit) {
        Timeout timeout = this.executorService.newTimeout(innerTimeout ->
                CancelledWheelScheduler.this.taskExecutor.execute(() -> {
                    try {
                        runnable.run();
                    } finally {
                        CancelledWheelScheduler.this.cancelledScheduledFutures.remove(key);
                    }
                }), delay, unit);
        this.replaceScheduledFuture(key, timeout);
    }

    public void shutdown() {
        this.executorService.stop();
    }

    private void replaceScheduledFuture(String key, Timeout newTimeout) {
        Timeout oldTimeout;
        if (newTimeout.isExpired()) {
            oldTimeout = this.cancelledScheduledFutures.remove(key);
        } else {
            oldTimeout = this.cancelledScheduledFutures.put(key, newTimeout);
        }

        if (oldTimeout != null) {
            oldTimeout.cancel();
        }
    }
}

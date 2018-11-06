package org.jordan.app.connect.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolUtil {
    private static ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
    static {
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(10);
        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(4);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(10);
        //线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(20);
//        poolTaskExecutor.setThreadNamePrefix("th-s");
        //当任务数量超过MaxPoolSize和QueueCapacity时使用的策略，该策略是又调用任务的线程执行
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        poolTaskExecutor.initialize();
    }
    public static Executor getExecutor() {
        return poolTaskExecutor;
    }
}

package com.fgrapp.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * @author fgr
 * @date 2022-11-21 19:30
 **/
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {
    /**
     * 异步任务线程池
     * 用于执行普通的异步请求，带有请求链路的MDC标志
     */
    @Bean
    public Executor commonThreadPool() {
        log.info("start init common thread pool");
        //ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        MdcTaskExecutor executor = new MdcTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(10);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(3000);
        //配置空闲线程存活时间
        executor.setKeepAliveSeconds(120);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("common-thread-pool-");
        //当达到最大线程池的时候丢弃最老的任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 定时任务线程池
     * 用于执行自启动的任务执行，父线程不带有MDC标志，不需要传递，直接设置新的MDC
     * 和上面的线程池没啥区别，只是名字不同
     */
    @Bean
    public Executor scheduleThreadPool() {
        log.info("start init schedule thread pool");
        MdcTaskExecutor executor = new MdcTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(3000);
        executor.setKeepAliveSeconds(120);
        executor.setThreadNamePrefix("schedule-thread-pool-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.initialize();
        return executor;
    }
}

package com.stackclonell.stackclone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Ensures a dedicated, bounded Thread Pool for all @Async tasks.
 * This prevents background tasks from consuming threads reserved for HTTP requests (Resilience/Scalability).
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        // Important: Set a descriptive thread name prefix for tracing/monitoring
        executor.setThreadNamePrefix("AsyncTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
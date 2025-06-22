package org.example.codebase.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfiguration {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // số thread tối thiểu
        executor.setMaxPoolSize(10); // số thread tối đa
        executor.setQueueCapacity(100); // số lượng task có thể chờ trong hàng đợi
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "cachedThreadPool")
    public Executor threadPoolTaskExecutor() {
        return Executors.newCachedThreadPool();
    }
}

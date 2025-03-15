package com.example.Personal_Travel_Concierge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "userServiceTaskExecutor")
    public ThreadPoolTaskExecutor userServiceTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the number of threads to start with
        executor.setMaxPoolSize(50); // Set the maximum number of threads
        executor.setQueueCapacity(100); // Set the capacity for the ThreadPoolTaskExecutor's blocking queue
        executor.setThreadNamePrefix("Async-"); // Set prefix for thread names
        executor.initialize();
        return executor;
    }

    @Bean(name = "bookingTaskExecutor")
    public ThreadPoolTaskExecutor bookingTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set the number of threads to start with
        executor.setMaxPoolSize(20); // Set the maximum number of threads
        executor.setQueueCapacity(50); // Set the capacity for the ThreadPoolTaskExecutor's blocking queue
        executor.setThreadNamePrefix("Async-"); // Set prefix for thread names
        executor.initialize();
        return executor;
    }

    @Bean(name = "commentTaskExecutor")
    public ThreadPoolTaskExecutor commentTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
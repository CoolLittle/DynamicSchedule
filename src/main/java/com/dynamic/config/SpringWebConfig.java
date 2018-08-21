package com.dynamic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages={"com.dynamic.config","com.dynamic.handler","com.dynamic.controller"})
@Slf4j
public class SpringWebConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 设置线程池容量
        threadPoolTaskScheduler.setPoolSize(100);
        // 线程名前缀
        threadPoolTaskScheduler.setThreadNamePrefix("task-dispatch-");
        threadPoolTaskScheduler.setAwaitTerminationSeconds(600);
        threadPoolTaskScheduler.setErrorHandler(throwable -> log.error("调度任务发生异常", throwable));
        // 当调度器shutdown被调用时等待当前被调度的任务完成
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(false);
        // 设置当任务被取消的同时从当前调度器移除的策略
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolTaskScheduler;
    }
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        return multipartResolver;
    }
}

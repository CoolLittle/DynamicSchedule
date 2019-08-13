package com.dynamic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages={"com.dynamic.*"})
@Slf4j
public class SpringWebConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 设置线程池容量
        threadPoolTaskScheduler.setPoolSize(20);
        // 线程名前缀
        threadPoolTaskScheduler.setThreadNamePrefix("schedule-task-");
        threadPoolTaskScheduler.setAwaitTerminationSeconds(600);
        threadPoolTaskScheduler.setErrorHandler(throwable -> log.error("调度任务发生异常", throwable));
        // 当调度器shutdown被调用时等待当前被调度的任务完成
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(false);
        // 设置当任务被取消的同时从当前调度器移除的策略
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolTaskScheduler;
    }
}

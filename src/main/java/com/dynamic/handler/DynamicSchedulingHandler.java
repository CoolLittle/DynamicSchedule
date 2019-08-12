package com.dynamic.handler;

import com.dynamic.constans.SpringScheduleConstants;
import com.dynamic.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class DynamicSchedulingHandler {

    @Autowired
    private TaskScheduler taskScheduler;

    private final Map<String, ScheduledTask> taskMap = new ConcurrentHashMap<>();

    private final Map<String, TriggerTask> triggerMap = new ConcurrentHashMap<>();

    /**
     * 添加任务
     *	如果任务已经存在，则取消原任务，创建新任务
     * @param taskId
     * @param triggerTask
     */
    public void addTriggerTask(String taskId, TriggerTask triggerTask) {
        if (hasTask(taskId)) {
            log.debug("the taskId [{}] was added，update triggerTask",taskId);
			cancelTriggerTask(taskId);
        }
        TaskScheduler scheduler = taskScheduler;
        ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
        try {
            Optional<Object> instance = BeanUtils.newInstance(ScheduledTask.class, triggerTask);
            if (instance.isPresent()) {
                ScheduledTask scheduledTask = (ScheduledTask) instance.get();
                BeanUtils.setFieldValue(scheduledTask, SpringScheduleConstants.FIELD_FUTURE_NAME, future);
                taskMap.put(taskId, scheduledTask);
                triggerMap.put(taskId, triggerTask);
            }
        } catch (Exception e) {
			log.error(e.getMessage());
        }
    }

    /**
     * 取消任务
     *
     * @param taskId
     */
    public void cancelTriggerTask(String taskId) {
        ScheduledTask task = taskMap.get(taskId);
        if (task != null) {
            task.cancel();
        }
		TriggerTask triggerTask = triggerMap.get(taskId);
        if(triggerTask !=null){
			triggerMap.remove(taskId);
		}
		taskMap.remove(taskId);
    }

    /**
     * 重置任务的间隔
     *
     * @param taskId
     * @param cron
     */
    public void resetTriggerTask(String taskId, String cron) {
        cancelTriggerTask(taskId);
        TriggerTask triggerTask = triggerMap.get(taskId);
        CronTrigger cronTrigger = new CronTrigger(cron);
        BeanUtils.setFieldValue(triggerTask, SpringScheduleConstants.FIELD_TRIGGER_NAME, cronTrigger);
        addTriggerTask(taskId, triggerTask);
    }

    /**
     * 任务编号
     *
     * @return
     */
    public Set<String> taskIds() {
        return taskMap.keySet();
    }

    /**
     * 是否存在任务
     *
     * @param taskId
     * @return
     */
    public boolean hasTask(String taskId) {
        return this.taskMap.containsKey(taskId);
    }

    /*public DynamicScheduledConfig getConfigurer() {
        return taskScheduler;
    }*/
}

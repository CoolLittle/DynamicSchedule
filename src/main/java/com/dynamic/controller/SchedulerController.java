package com.dynamic.controller;

import com.dynamic.constans.TaskCronConstants;
import com.dynamic.handler.DynamicSchedulingHandler;
import com.dynamic.util.HttpStatus;
import com.dynamic.util.JsonResultObject;
import com.dynamic.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(value = "/scheduled")
@Controller
@Slf4j
public class SchedulerController {

    @Autowired
    private DynamicSchedulingHandler handler;

    /**
     * 添加新的定时任务
     * @param param
     *        taskId 任务ID
     *        cron 执行时间 示例：0 0/1 * 21 8 ?
     *        path 待执行的方法全路径名 示例：com.dynamic.customer.CustomerMethod.print
     * @return
     */
    @PostMapping("/addscheduletask")
    @ResponseBody
    public JsonResultObject<Object> addScheduleTask(@RequestBody Map<String, String> param) {
        JsonResultObject<Object> jsonResultObject = new JsonResultObject<>();
        String taskId = param.get("id");
        String cron = param.get("cron");
        String classPath = param.get("classPath");
        String methodName = param.get("methodName");
        if ("".equals(taskId) || "".equals(cron) ||"".equals(classPath) || "".equals(methodName)) {
            jsonResultObject.setCode(HttpStatus.FAILURE.getValue());
            jsonResultObject.setData(null);
            jsonResultObject.setInfo(TaskCronConstants.ADD_FAIL.getMessage());
            return jsonResultObject;
        }
        try {
            TriggerTask triggerTask = ScheduleUtils.getTriggerTask(classPath,methodName,cron);
            handler.addTriggerTask(taskId,triggerTask);
        }catch (Exception e){
            jsonResultObject.setCode(HttpStatus.FAILURE.getValue());
            jsonResultObject.setData(null);
            jsonResultObject.setInfo(TaskCronConstants.ADD_FAIL.getMessage());
            log.error("添加定时任务异常：{}",e);
            return jsonResultObject;
        }
        jsonResultObject.setCode(HttpStatus.SUCCESS.getValue());
        jsonResultObject.setData(null);
        jsonResultObject.setInfo(TaskCronConstants.ADD_SUCCESS.getMessage());
        return jsonResultObject;
    }

    /**
     * 修改定时任务执行时间
     * @param taskId 任务ID
     * @param cron 执行时间
     * @return
     */
    @PostMapping("/reset")
    public String resetTaskCron(@RequestParam("taskId") String taskId,
                                @RequestParam("cron") String cron) {

        if ("".equals(taskId) || "".equals(cron)) {
            return TaskCronConstants.RESET_FAIL.getMessage();
        }
        try {
            handler.resetTriggerTask(taskId, cron);
        }catch (Exception e){
            log.error("修改定时任务异常：{}",e);
        }
        return TaskCronConstants.RESET_SUCCESS.getMessage();
    }
}


package com.dynamic.controller;

import com.dynamic.constans.TaskCronConstants;
import com.dynamic.handler.DynamicSchedulingHandler;
import com.dynamic.util.HttpStatus;
import com.dynamic.util.JsonResult;
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
    @Autowired
    private ScheduleUtils scheduleUtils;

    /**
     * 添加新的定时任务
     * @param param
     *        taskId 任务ID
     *        cron 执行时间 示例：0 0/1 * 21 8 ?
     *        path 待执行的方法全路径名 示例：com.dynamic.customer.CustomerMethod.print
     * @return
     */
    @PostMapping("/add-task")
    @ResponseBody
    public JsonResult addScheduleTask(@RequestBody Map<String, String> param) {
		JsonResult json = new JsonResult();
        String taskId = param.get("id");
        String cron = param.get("cron");
        String classPath = param.get("classPath");
        String methodName = param.get("methodName");
        if ("".equals(taskId) || "".equals(cron) ||"".equals(classPath) || "".equals(methodName)) {
            json.setCode(HttpStatus.FAILURE.getValue());
            json.setInfo(TaskCronConstants.ADD_FAIL.getMessage());
            return json;
        }
        try {
            TriggerTask triggerTask = scheduleUtils.getTriggerTask(classPath,methodName,cron);
            handler.addTriggerTask(taskId,triggerTask);
        }catch (Exception e){
            json.setCode(HttpStatus.FAILURE.getValue());
            json.setInfo(TaskCronConstants.ADD_FAIL.getMessage());
            log.error("添加定时任务异常：{}",e);
            return json;
        }
        json.setCode(HttpStatus.SUCCESS.getValue());
        json.setInfo(TaskCronConstants.ADD_SUCCESS.getMessage());
        return json;
    }

    @GetMapping("cancel-task")
	@ResponseBody
    public JsonResult cancelTask(@RequestParam(name = "taskId") String taskId){
		JsonResult json = new JsonResult();
		try{
    		handler.cancelTriggerTask(taskId);
		}catch (Exception e){
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.DEL_FAIL.getMessage());
			log.error("取消定时任务异常：{}",e);
			return json;
		}
		json.setCode(HttpStatus.SUCCESS.getValue());
		json.setInfo(TaskCronConstants.DEL_SUCCESS.getMessage());
		return json;
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


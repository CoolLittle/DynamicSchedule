package com.dynamic.moduler.controller;

import com.dynamic.constans.TaskCronConstants;
import com.dynamic.moduler.entity.TaskScheduleEntity;
import com.dynamic.moduler.service.ITaskScheduleService;
import com.dynamic.moduler.vo.TaskScheduleVo;
import com.dynamic.util.HttpStatus;
import com.dynamic.util.JsonResult;
import com.dynamic.util.JsonResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(value = "/scheduled")
@Controller
@Slf4j
public class SchedulerController {

	@Autowired
	private ITaskScheduleService taskScheduleService;

	/**
	 * 添加新的定时任务
	 * @param taskScheduleVo
	 *        taskId 任务ID
	 *        cron 执行时间 示例：0 0/1 * 21 8 ?
	 *        classPath 待执行的方法全路径名 示例：com.dynamic.customer.CustomerMethod
	 *        methodName 待执行的方法名 示例：print
	 * @return
	 */
    @PostMapping("/add-task")
    @ResponseBody
    public JsonResult addScheduleTask(@RequestBody TaskScheduleVo taskScheduleVo) {

		JsonResult json = new JsonResult();
        if ("".equals(taskScheduleVo.getClassPath()) || "".equals(taskScheduleVo.getMethodName())
				|| "".equals(taskScheduleVo.getCron())) {
            json.setCode(HttpStatus.FAILURE.getValue());
            json.setInfo(TaskCronConstants.ADD_FAIL.getMessage());
            return json;
        }
        try {
			taskScheduleService.addTask(taskScheduleVo);
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

	@PostMapping("/update-task")
	@ResponseBody
	public JsonResult updateScheduleTask(@RequestBody TaskScheduleVo taskScheduleVo) {

		JsonResult json = new JsonResult();
		if ("".equals(taskScheduleVo.getClassPath()) || "".equals(taskScheduleVo.getMethodName())
				|| "".equals(taskScheduleVo.getCron())) {
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.UPD_FAIL.getMessage());
			return json;
		}
		try {
			taskScheduleService.updateTask(taskScheduleVo);
		}catch (Exception e){
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.UPD_FAIL.getMessage());
			log.error("添加定时任务异常：{}",e);
			return json;
		}
		json.setCode(HttpStatus.SUCCESS.getValue());
		json.setInfo(TaskCronConstants.UPD_SUCCESS.getMessage());
		return json;
	}

    @GetMapping("/get-task")
	@ResponseBody
    public JsonResult getTask(@RequestParam(name = "taskId") Integer taskId){

		JsonResult json = new JsonResultObject();
		try{
			TaskScheduleEntity entity = taskScheduleService.getById(taskId);
			((JsonResultObject) json).setData(entity);
		}catch (Exception e){
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.GET_FAIL.getMessage());
			log.error("获取定时任务异常：{}",e);
			return json;
		}
		json.setCode(HttpStatus.SUCCESS.getValue());
		json.setInfo(TaskCronConstants.GET_SUCCESS.getMessage());
		return json;
    }

    @GetMapping("/list-task")
	@ResponseBody
    public JsonResult listTask(){

		JsonResult json = new JsonResultObject<>();
		try{
			List<TaskScheduleEntity> list = taskScheduleService.list();
			((JsonResultObject) json).setData(list);
		}catch (Exception e){
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.GET_FAIL.getMessage());
			log.error("获取定时任务异常：{}",e);
			return json;
		}

		json.setCode(HttpStatus.SUCCESS.getValue());
		json.setInfo(TaskCronConstants.GET_SUCCESS.getMessage());
		return json;
    }

    @GetMapping("/execute-task")
	@ResponseBody
    public JsonResult executeTask(@RequestParam(name = "taskId") Integer taskId){

		JsonResult json = new JsonResult();
		try{
			taskScheduleService.executeTask(taskId);
		}catch (Exception e){
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.EXC_FAIL.getMessage());
			log.error("执行定时任务异常：{}",e);
			return json;
		}
		json.setCode(HttpStatus.SUCCESS.getValue());
		json.setInfo(TaskCronConstants.EXC_SUCCESS.getMessage());
		return json;
    }

    @GetMapping("/cancel-task")
	@ResponseBody
    public JsonResult cancelTask(@RequestParam(name = "taskId") Integer taskId){

		JsonResult json = new JsonResult();
		try{
			taskScheduleService.cancelTask(taskId);
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

    @GetMapping("/manual-task")
	@ResponseBody
    public JsonResult manualTask(@RequestParam(name = "taskId") Integer taskId){

		JsonResult json = new JsonResult();
		try{
			boolean flag = taskScheduleService.manualExecuteTask(taskId);
			if(flag){
				json.setInfo("手动执行成功");
			}else {
				json.setInfo("手动执行失败");
			}
		}catch (Exception e){
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.EXC_FAIL.getMessage());
			log.error("手动执行定时任务异常：{}",e);
			return json;
		}
		json.setCode(HttpStatus.SUCCESS.getValue());
		return json;
    }

    /**
     * 修改定时任务执行时间
     * @param  param { taskId:任务ID,cron:执行时间 }
     * @return
     */
    @PostMapping("/reset-task")
	@ResponseBody
    public JsonResult resetTaskCron(@RequestBody Map<String, String> param) {

		JsonResult json = new JsonResult();
		try{
			String taskId = param.get("taskId");
			String cron = param.get("cron");
			if ("".equals(taskId) || "".equals(cron)) {
				json.setCode(HttpStatus.FAILURE.getValue());
				json.setInfo(TaskCronConstants.RESET_FAIL.getMessage());
				return json;
			}
			try {
				taskScheduleService.resetCorn(Integer.valueOf(taskId), cron);
			}catch (Exception e){
				log.error("修改定时任务异常：{}",e);
			}
		}catch (Exception e){
			json.setCode(HttpStatus.FAILURE.getValue());
			json.setInfo(TaskCronConstants.DEL_FAIL.getMessage());
			log.error("取消定时任务异常：{}",e);
			return json;
		}
		json.setCode(HttpStatus.SUCCESS.getValue());
		json.setInfo(TaskCronConstants.RESET_SUCCESS.getMessage());
		return json;
    }
}


package com.dynamic.moduler.service.imp;

import com.dynamic.constans.TaskStatusConstants;
import com.dynamic.handler.DynamicSchedulingHandler;
import com.dynamic.moduler.dao.TaskScheduleDao;
import com.dynamic.moduler.entity.TaskScheduleEntity;
import com.dynamic.moduler.service.ITaskScheduleService;
import com.dynamic.moduler.vo.TaskScheduleVo;
import com.dynamic.util.ScheduleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @description:
 * @author:
 * @date: 2019/8/13 10:49
 */
@Service("taskScheduleService")
public class TaskScheduleServiceImpl implements ITaskScheduleService {

	@Autowired
	private TaskScheduleDao taskScheduleDao;

	@Autowired
	private DynamicSchedulingHandler schedulingHandler;

	@Autowired
	private ScheduleUtils scheduleUtils;

	@Override
	public TaskScheduleEntity getById(Integer id) {
		return taskScheduleDao.selectById(id);
	}

	@Override
	public List<TaskScheduleEntity> list() {
		return taskScheduleDao.select();
	}

	@Override
	public boolean addTask(TaskScheduleVo taskScheduleVo) throws Exception {
		Integer id = taskScheduleDao.add(taskScheduleVo.getTaskName(),taskScheduleVo.getParam(),taskScheduleVo.getClassPath(),taskScheduleVo.getMethodName(),taskScheduleVo.getCron());
		return false;
	}

	/**
	 * 取消任务
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteById(Integer id) {
		schedulingHandler.cancelTriggerTask(id);
		taskScheduleDao.delete(id);
		return false;
	}

	/**
	 * 执行任务
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean executeTask(Integer id) throws Exception{

		TaskScheduleEntity taskScheduleEntity = getById(id);
		TriggerTask triggerTask = scheduleUtils.getTriggerTask(taskScheduleEntity.getClassPath(),taskScheduleEntity.getMethodName(),taskScheduleEntity.getCron());
		schedulingHandler.addTriggerTask(id,triggerTask);
		// 设置为取消状态
		taskScheduleDao.updateStatus(id,TaskStatusConstants.执行.getValue());
		return true;
	}

	/**
	 * 取消执行任务
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean cancelTask(Integer id) {

		// 设置为取消状态
		taskScheduleDao.updateStatus(id,TaskStatusConstants.未执行.getValue());
		schedulingHandler.cancelTriggerTask(id);
		return true;
	}

	/**
	 * 重置任务执行时间
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean resetCorn(Integer id,String cron) throws Exception {

		taskScheduleDao.updateCron(id,cron);
		schedulingHandler.resetTriggerTask(id,cron);
		return false;
	}
}

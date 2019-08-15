package com.dynamic.moduler.service.imp;

import com.dynamic.constans.TaskStatusConstants;
import com.dynamic.handler.DynamicSchedulingHandler;
import com.dynamic.moduler.dao.TaskScheduleDao;
import com.dynamic.moduler.entity.TaskScheduleEntity;
import com.dynamic.moduler.service.ITaskScheduleService;
import com.dynamic.moduler.vo.TaskScheduleVo;
import com.dynamic.util.BeanUtils;
import com.dynamic.util.JsonKit;
import com.dynamic.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author:
 * @date: 2019/8/13 10:49
 */
@Slf4j
@Service("taskScheduleService")
public class TaskScheduleServiceImpl implements ITaskScheduleService {

	@Autowired
	private TaskScheduleDao taskScheduleDao;

	@Autowired
	private DynamicSchedulingHandler schedulingHandler;

	@Autowired
	private ScheduleUtils scheduleUtils;

	@Autowired
	@Lazy(value = false)
	private ApplicationContext applicationContext;

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
	 * 修改定时任务
	 *
	 * @param taskScheduleVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean updateTask(TaskScheduleVo taskScheduleVo) throws Exception {
		taskScheduleDao.updateEntity(taskScheduleVo.getTaskId(),taskScheduleVo.getTaskName(),taskScheduleVo.getParam(),taskScheduleVo.getClassPath(),taskScheduleVo.getMethodName(),taskScheduleVo.getCron());
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

		Class[] types = null;
		Object[] param = null;
		if(taskScheduleEntity.getParam() !=null && !"".equals(taskScheduleEntity.getParam())){

			List<Map<String,Object>> list = JsonKit.parseArray(taskScheduleEntity.getParam());
			if(list!=null){
				types = new Class[list.size()];
				param = new Object[list.size()];
				int i = 0;
				for (Map<String,Object> m : list){
					types[i] = BeanUtils.convert(m.get("Type").toString());
					param[i] = m.get("Value");
					i++;
				}
			}
		}
		TriggerTask triggerTask = scheduleUtils.getTriggerTask(taskScheduleEntity.getClassPath(),
				taskScheduleEntity.getMethodName(),taskScheduleEntity.getCron(),types,param);
		schedulingHandler.addTriggerTask(id,triggerTask);
		// 设置为取消状态
		taskScheduleDao.updateStatus(id,TaskStatusConstants.执行.getValue());
		return true;
	}

	/**
	 * 手动执行任务
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean manualExecuteTask(Integer id) throws Exception {

		TaskScheduleEntity taskScheduleEntity = getById(id);
		if(taskScheduleEntity == null){
			return false;
		}
		Class clazz;
		Class[] types = null;
		Object[] param = null;
		if(taskScheduleEntity.getParam() !=null && !"".equals(taskScheduleEntity.getParam())){

			List<Map<String,Object>> list = JsonKit.parseArray(taskScheduleEntity.getParam());
			if(list!=null){
				types = new Class[list.size()];
				param = new Object[list.size()];
				int i = 0;
				for (Map<String,Object> m : list){
					types[i] = BeanUtils.convert(m.get("Type").toString());
					param[i] = m.get("Value");
					i++;
				}
			}
		}
		try {
			clazz = Class.forName(taskScheduleEntity.getClassPath());
		}catch (ClassNotFoundException e){
			log.error("未找到该类：{}",e);
			return false;
		}

		Object object = null;
		try {
			// 如果已经注入该类，则直接返回实例，不再创建
			if(clazz != null){
				object = applicationContext.getBean(clazz);
			}
		}catch (NoSuchBeanDefinitionException e){
			log.debug("执行类未找到该Bean");
			object = null;
		}
		try {
			// 如果在SpringBean中未找到Bean实例
			if(object == null){
				object = clazz.newInstance();
			}
			Method method = clazz.getMethod(taskScheduleEntity.getMethodName(),types);
			method.invoke(object,param);
		}catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
			log.error("初始化异常：{}",e);
			return false;
		}
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
	 * 重新开始启动执行任务
	 *
	 * @return
	 */
	@Override
	public void startup() throws Exception {

		// 获取现有运行的定时器
		Set<Integer> ids = schedulingHandler.taskIds();
		for (Integer id : ids){
			schedulingHandler.cancelTriggerTask(id);
		}
		// 启动在运行状态的定时器
		List<TaskScheduleEntity> list = taskScheduleDao.select(TaskStatusConstants.执行.getValue());
		for(TaskScheduleEntity t : list){
			executeTask(t.getTaskId());
		}
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

package com.dynamic.moduler.service;

import com.dynamic.moduler.entity.TaskScheduleEntity;
import com.dynamic.moduler.vo.TaskScheduleVo;

import java.util.List;

/**
 * @description:
 * @author:
 * @date: 2019/8/13 10:49
 */
public interface ITaskScheduleService {

	/**
	 * 通过ID获取任务
	 * @param id
	 * @return
	 */
	TaskScheduleEntity getById(Integer id) throws Exception;

	/**
	 * 获取所有任务列表
	 * @return
	 */
	List<TaskScheduleEntity> list() throws Exception;

	/**
	 * 新增定时任务
	 * @param taskScheduleVo
	 * @return
	 * @throws Exception
	 */
	boolean addTask(TaskScheduleVo taskScheduleVo) throws Exception;

	/**
	 * 修改定时任务
	 * @param taskScheduleVo
	 * @return
	 * @throws Exception
	 */
	boolean updateTask(TaskScheduleVo taskScheduleVo) throws Exception;


	/**
	 * 删除任务
	 * @param id
	 * @return
	 */
	boolean deleteById(Integer id) throws Exception;

	/**
	 * 执行任务
	 * @return
	 */
	boolean executeTask(Integer id) throws Exception;

	/**
	 * 手动执行任务
	 * @return
	 */
	boolean manualExecuteTask(Integer id) throws Exception;

	/**
	 * 取消执行任务
	 * @return
	 */
	boolean cancelTask(Integer id) throws Exception;

	/**
	 * 服务器启动后执行任务
	 * @return
	 */
	void startup() throws Exception;

	/**
	 * 重置任务执行时间
	 * @return
	 */
	boolean resetCorn(Integer id,String cron) throws Exception;

}

package com.dynamic.moduler.vo;

import lombok.Data;

/**
 * @description:
 * @author: caozheng
 * @date: 2019/8/13 11:31
 */
@Data
public class TaskScheduleVo {

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 待执行类路径
	 */
	private String classPath;

	/**
	 * 待执行方法名
	 */
	private String methodName;

	/**
	 * 执行时间
	 */
	private String cron;

	/**
	 * 入参
	 */
	private String param;

	/**
	 * 优先级
	 */
	private Integer priority;

}

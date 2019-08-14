package com.dynamic.config;

import com.dynamic.moduler.service.ITaskScheduleService;

/**
 * @description:
 * 		服务器启动时初始化的Bean执行
 * @author: caozheng
 * @date: 2019/8/14 10:30
 */
public class InitBean {

	private ITaskScheduleService taskScheduleService;

	InitBean(ITaskScheduleService taskScheduleService){
		this.taskScheduleService = taskScheduleService;
	}

	public void init() throws Exception{
		taskScheduleService.startup();
	}

}

package com.dynamic.config.listener;

import com.dynamic.customer.CustomerMethod2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @description:
 * @author: caozheng
 * @date: 2019/8/14 10:04
 */
@Slf4j
public class ScheduleListener implements ServletContextListener {

	@Autowired
	private CustomerMethod2 customerMethod2;

	/**
	 * 启动时
	 * @param sce
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("contextInitialized");
	}


	/**
	 * 销毁时
	 * @param sce
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("contextDestroyed");
	}
}

package com.dynamic.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Component
public class ScheduleUtils {

	@Autowired
	private ApplicationContext applicationContext;

    public TriggerTask getTriggerTask(String classPath,String methodName, String cron,Class[] types,Object[] params) throws Exception{

		Runnable runnable ;
    	try {
			runnable = () -> {
				Class clazz;
				try {
					clazz = Class.forName(classPath);
				}catch (ClassNotFoundException e){
					log.error("未找到该类：{}",e);
					return;
				}

				Object object = null;
				try {
					// 如果已经注入该类，则直接返回实例，不再创建
					if(clazz != null){
						object = applicationContext.getBean(clazz);
					}
				}catch (NoSuchBeanDefinitionException e){
					log.debug("定时器执行类未找到该Bean");
					object = null;
				}
				try {
					// 如果在SpringBean中未找到Bean实例
					if(object == null){
						object = clazz.newInstance();
					}
					Method method = clazz.getMethod(methodName,types);
					method.invoke(object,params);
				}catch (InstantiationException e){
					log.error("定时器执行类初始化异常：{}",e);
				}catch (IllegalAccessException e){
					log.error("非法访问异常：{}",e);
				}catch (NoSuchMethodException e){
					log.error("无本方法异常：{}",e);
				}catch (InvocationTargetException e){
					log.error("目标调用异常：{}",e);
				}finally {
					return;
				}
			};

			TriggerTask triggerTask = new TriggerTask(runnable, (TriggerContext triggerContext) -> {
				CronTrigger trigger = new CronTrigger(cron);
				return trigger.nextExecutionTime(triggerContext);
			});
			return triggerTask;
		}catch (Exception e){
    		throw  e;
		}
    }
}

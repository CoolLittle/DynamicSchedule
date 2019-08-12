package com.dynamic.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ScheduleUtils {

    public static TriggerTask getTriggerTask(String classPath,String methodName, String cron) throws Exception{



        TriggerTask triggerTask = new TriggerTask(() -> {
            try {
                Class clazz = Class.forName(classPath);
                Object object = clazz.newInstance();
                Method method = clazz.getMethod(methodName);
                method.invoke(object,null);
            }catch (ClassNotFoundException e){
                log.error("定时器执行类未找到：{}",e);
            }catch (InstantiationException e){
				log.error("定时器执行类初始化异常：{}",e);
            }catch (IllegalAccessException e){
				log.error("非法访问异常：{}",e);
            }catch (NoSuchMethodException e){
				log.error("无本方法异常：{}",e);
            }catch (InvocationTargetException e){
				log.error("目标调用异常：{}",e);
            }
        }, (TriggerContext triggerContext) -> {
            CronTrigger trigger=new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
        return triggerTask;
    }
}

package com.dynamic.util;

import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScheduleUtils {

    public static TriggerTask getTriggerTask(String classPath,String methodName, String cron) throws Exception{


        TriggerTask triggerTask = new TriggerTask(() -> {
            try {
                Class clazz = Class.forName(classPath);
                Object object = clazz.newInstance();
                Method method = clazz.getMethod(methodName);
                method.invoke(object,null);
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }catch (InstantiationException e){
                e.printStackTrace();
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            }catch (InvocationTargetException e){
                e.printStackTrace();
            }
        }, (TriggerContext triggerContext) -> {
            CronTrigger trigger=new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
        return triggerTask;
    }
}

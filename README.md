# DynamicSchedule
动态配置Spring定时器

    添加定时任务：/scheduled/add-task 前端传递任务ID,cron表达式、待执行类路径、待执行方法名。
    
    取消定时任务：/scheduled/cancel-task 前端传递任务ID即可取消任务。
	
	重置定时任务执行时间：/scheduled/reset-task 前端传递待修改任务ID,新的cron表达式即可修改执行时间。

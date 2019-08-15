# DynamicSchedule
动态配置Spring定时器

    添加定时任务：/scheduled/add-task 前端传递任务ID,cron表达式、待执行类路径、待执行方法名。
    
    修改定时任务：/scheduled/update-task 前端传递任务ID,cron表达式、待执行类路径、待执行方法名可以修改定时任务内容。
    
    获取单个定时任务：/scheduled/get-task 前端传递任务ID即可获取。
    
    获取定时任务列表：/scheduled/list-task 调用即可获取。
    
    取消定时任务：/scheduled/cancel-task 前端传递任务ID即可取消任务。
    
    手动执行一次定时任务：/scheduled/manual-task 前端传递任务ID即可手动执行一次任务。
    
    开始执行定时任务：/scheduled/execute-task 前端传递任务ID即可按照cron表达式开始任务。
	
	重置定时任务执行时间：/scheduled/reset-task 前端传递待修改任务ID,新的cron表达式即可修改执行时间。

package com.dynamic.moduler.entity;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @description:	任务实体
 * @author:
 * @date: 2019/8/13 10:39
 */
@Data
public class TaskScheduleEntity implements RowMapper<TaskScheduleEntity>,Serializable {

	/**
	 * 任务ID
	 */
	private Integer taskId;

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

	/**
	 * 执行状态
	 */
	private Integer executeStatus;

	/**
	 * 执行时间
	 */
	private Timestamp executeTime;

	/**
	 * 添加时间
	 */
	private Timestamp addTime;

	@Override
	public TaskScheduleEntity mapRow(ResultSet rs, int i) throws SQLException {

		TaskScheduleEntity task = new TaskScheduleEntity();

		task.setTaskId(rs.getInt("task_id"));
		task.setTaskName(rs.getString("task_name"));
		task.setClassPath(rs.getString("class_path"));
		task.setMethodName(rs.getString("method_name"));
		task.setCron(rs.getString("cron"));
		task.setParam(rs.getString("param"));
		task.setPriority(rs.getInt("priority"));
		task.setExecuteTime(rs.getTimestamp("execute_time"));
		task.setAddTime(rs.getTimestamp("add_time"));
		task.setExecuteStatus(rs.getInt("execute_status"));
		return task;
	}
}

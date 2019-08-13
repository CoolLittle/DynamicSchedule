package com.dynamic.moduler.dao;

import com.dynamic.constans.TaskStatusConstants;
import com.dynamic.moduler.entity.TaskScheduleEntity;
import com.mysql.jdbc.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description:
 * @author:
 * @date: 2019/8/13 10:47
 */
@Repository
public class TaskScheduleDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 通过任务ID,获取定时任务
	 * @param id
	 * @return
	 */
	public TaskScheduleEntity selectById(Integer id){
		String sql = " select * from  p_task_schedule where task_id = ? ";
		Object[] param = {id};
		return jdbcTemplate.queryForObject(sql,param,new TaskScheduleEntity());
	}

	/**
	 * 获取所有定时任务
	 * @return
	 */
	public List<TaskScheduleEntity> select(){
		String sql = " select * from  p_task_schedule ";
		return jdbcTemplate.query(sql,new TaskScheduleEntity());
	}

	/**
	 * 添加
	 * @param taskName
	 * @param param
	 * @param classPath
	 * @param methodName
	 * @param cron
	 * @return
	 */
	public Integer add(String taskName,String param,String classPath,String methodName,String cron){

		String sql = " insert into p_task_schedule(task_name,class_path,method_name,param,cron,execute_status,add_time) values(?,?,?,?,?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		return jdbcTemplate.update(conn ->{
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i = 0;
			ps.setString(++i,taskName);
			ps.setString(++i,classPath );
			ps.setString(++i,methodName );
			ps.setString(++i,param );
			ps.setString(++i,cron );
			ps.setInt(++i, TaskStatusConstants.未执行.getValue());
			ps.setTimestamp(++i,new Timestamp(System.currentTimeMillis()));
			return ps;
		},keyHolder);
	}

	/**
	 * 更新状态
	 * @param taskId
	 * @param status
	 * @return
	 */
	public Integer updateStatus(int taskId,int status){

		String sql = " update  p_task_schedule set execute_status = ?,execute_time = ? where task_id = ? ";
		Object[] param = {status,new Timestamp(System.currentTimeMillis()),taskId};
		return jdbcTemplate.update(sql,param);
	}

	/**
	 * 更新执行cron表达式
	 * @param taskId
	 * @return
	 */
	public Integer updateCron(int taskId,String cron){

		String sql = " update  p_task_schedule set execute_time = ?,cron = ? where task_id = ? ";
		Object[] param = {new Timestamp(System.currentTimeMillis()),cron,taskId};
		return jdbcTemplate.update(sql,param);
	}
	/**
	 * 更新执行时间
	 * @param taskId
	 * @return
	 */
	public Integer updateExecuteTime(int taskId){

		String sql = " update  p_task_schedule set execute_time = ? where task_id = ? ";
		Object[] param = {new Timestamp(System.currentTimeMillis()),taskId};
		return jdbcTemplate.update(sql,param);
	}

	/**
	 * 删除定时任务
	 * @param taskId
	 * @return
	 */
	public Integer delete(int taskId){

		String sql = " delete  p_task_schedule where task_id = ? ";
		Object[] param = {taskId};
		return jdbcTemplate.update(sql,param);
	}

}


-- ----------------------------
-- Table structure for p_task_schedule
-- ----------------------------
DROP TABLE IF EXISTS `p_task_schedule`;
CREATE TABLE `p_task_schedule` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_name` varchar(80) DEFAULT NULL COMMENT '任务名称',
  `class_path` varchar(100) DEFAULT NULL COMMENT '类所在路径',
  `method_name` varchar(80) DEFAULT NULL COMMENT '待执行方法',
  `cron` varchar(40) DEFAULT NULL COMMENT 'cron表达式',
  `param` varchar(100) DEFAULT NULL COMMENT '参数',
  `priority` varchar(255) DEFAULT NULL COMMENT '执行优先级',
  `exectue_status` tinyint(2) DEFAULT '0' COMMENT '执行状态 0 待执行  1执行中',
  `execute_time` datetime DEFAULT NULL COMMENT '任务最后执行的时间',
  `add_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

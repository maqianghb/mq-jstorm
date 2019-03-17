package com.example.mq.jstorm.monitor.service;

import java.util.List;

import com.example.mq.jstorm.monitor.bean.MonitorTaskConfig;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2019/3/15
 *
 */
public interface TaskConfigService {

	/**
	 * 获取开起监控的task的配置信息
	 * @return
	 * @throws Exception
	 */
	List<MonitorTaskConfig> getOpenMonitorTasks() throws Exception;
}

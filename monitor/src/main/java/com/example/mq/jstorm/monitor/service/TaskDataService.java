package com.example.mq.jstorm.monitor.service;

import java.util.Map;

import com.example.mq.jstorm.monitor.bean.MonitorTaskConfig;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2019/3/15
 *
 */
public interface TaskDataService {

	/**
	 * 查询task在监控时间的运行数据
	 * @param taskConfig
	 * @param monitorTime
	 * @return
	 * @throws Exception
	 */
	Map<String, Double> queryTaskData(MonitorTaskConfig taskConfig, long monitorTime) throws Exception;
}

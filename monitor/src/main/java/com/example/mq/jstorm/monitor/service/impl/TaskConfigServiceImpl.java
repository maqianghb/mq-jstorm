package com.example.mq.jstorm.monitor.service.impl;

import java.util.List;

import com.example.mq.jstorm.monitor.bean.MonitorTaskConfig;
import com.example.mq.jstorm.monitor.service.TaskConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/24
 *
 */
@Service
public class TaskConfigServiceImpl implements TaskConfigService {
	private static final Logger LOG = LoggerFactory.getLogger(TaskConfigServiceImpl.class);

	@Override
	public List<MonitorTaskConfig> getOpenMonitorTasks() throws Exception{
		return null;
	}


}

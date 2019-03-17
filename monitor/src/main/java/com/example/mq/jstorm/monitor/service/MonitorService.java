package com.example.mq.jstorm.monitor.service;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/24
 *
 */
public interface MonitorService {

	/**
	 * 异常检测及告警
	 * @param monitorTime
	 * @throws Exception
	 */
	void checkTaskAbnormalAndNotify(Long monitorTime) throws Exception;

}

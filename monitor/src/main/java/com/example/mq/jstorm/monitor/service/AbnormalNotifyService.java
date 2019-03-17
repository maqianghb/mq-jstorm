package com.example.mq.jstorm.monitor.service;

import java.util.List;

import com.example.mq.jstorm.monitor.bean.AbnormalDetail;
import com.example.mq.jstorm.monitor.bean.MonitorTaskConfig;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2019/3/15
 *
 */
public interface AbnormalNotifyService {

	/**
	 *异常时是否需要告警
	 * @param taskId
	 * @param monitorTime
	 * @return
	 * @throws Exception
	 */
	boolean isNeedNotify(long taskId, long monitorTime, long abnormalLimitTimes) throws Exception;

	/**
	 * 告警通知
	 * @param taskConfig
	 * @param monitorTime
	 * @param abnormalDetails
	 * @return
	 * @throws Exception
	 */
	int notify(MonitorTaskConfig taskConfig, long monitorTime, List<AbnormalDetail> abnormalDetails) throws Exception;

}

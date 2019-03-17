package com.example.mq.jstorm.monitor.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/25
 *
 */
@Component
public class MongoConstant {

	/**
	 * 任务运行数据存储
	 */
	@Value("${mongodb.monitor.task.data.collection}")
	public String MONITOR_TASK_DATA;

	/**
	 * 发送消息记录存储
	 */
	@Value("${mongodb.monitor.notify.collection}")
	public String MONITOR_NOTIFY_RECORD;

}

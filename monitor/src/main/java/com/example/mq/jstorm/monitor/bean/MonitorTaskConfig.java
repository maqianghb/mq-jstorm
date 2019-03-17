package com.example.mq.jstorm.monitor.bean;

import java.util.List;

import lombok.Data;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2019/3/15
 *
 */
@Data
public class MonitorTaskConfig {

	private Long taskId;

	private String taskName;

	/**
	 * 集群名
	 */
	private String clusterName;

	/**
	 * 拓朴名
	 */
	private String topologyName;

	/**
	 * 开始监控时间
	 * 格式：hh:MM:ss
	 */
	private String startMonitorTime;

	/**
	 * 结束监控时间
	 *
	 */
	private String endMonitorTime;

	/**
	 * 监控指标及阈值
	 */
	private List<MonitorIndex> monitorIndexes;

	/**
	 * 触发告警条件，10分钟内异常出现次数
	 */
	private Integer notifyCondition;

	/**
	 * 通知方式
	 */
	private List<Integer> notifyTypes;

	/**
	 * 短信通知手机号列表
	 */
	private List<String> smsPhones;

	/**
	 * 邮件通知地址列表
	 */
	private List<String> mailAdds;

	/**
	 * 每天通知次数
	 */
	private Integer notifyTimesPerDay;

}

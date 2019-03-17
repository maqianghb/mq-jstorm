package com.example.mq.jstorm.monitor.bean;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/25
 *
 */
@Data
public class MonitorDataRecord {

	private Long taskId;

	private String taskName;

	/**
	 * 监控时间戳
	 * 格式：yyyyMMddHHmm
	 */
	private String monitorTimestamp;

	/**
	 *监控时间对应的指标值
	 */
	private Map<String, Double> taskRunDatas;

	/**
	 * 该时刻指标值是否正常
	 * 取值见：TaskStatusEnum，1：正常，2：异常
	 */
	private Integer status;

	/**
	 * 指标异常详情
	 */
	private List<AbnormalDetail> abnormalDetails;

	/**
	 * 记录创建时间
	 */
	private Long createTime;
}

package com.example.mq.jstorm.monitor.bean;

import lombok.Data;

/**
 * @program: mq-jstorm
 * @description: 监控指标及正常值设定
 * @author: maqiang
 * @create: 2018/12/24
 *
 */
@Data
public class MonitorIndex {

	/**
	 * 比较指标名称
	 */
	private String indexName;

	/**
	 * 同比类型
	 * 1:与前10分钟平均值相比，2：与上一天同期值相比，3：与上一周同期平均值相比
	 *
	 */
	private Integer compareType;

	/**
	 * 同比浮动比例
	 */
	private Double overLimitRadio;

	/**
	 * 最小值
	 */
	private Double minLimitValue;
}

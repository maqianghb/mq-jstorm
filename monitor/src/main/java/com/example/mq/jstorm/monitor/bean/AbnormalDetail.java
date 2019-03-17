package com.example.mq.jstorm.monitor.bean;

import lombok.Data;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/28
 *
 */
@Data
public class AbnormalDetail {

	/**
	 * 拓扑名称
	 */
	private String topologyName;

	/**
	 * 时间
	 */
	private long monitorTime;

	/**
	 * 指标名称
	 */
	private String indexName;

	/**
	 * 指标值
	 */
	private Double indexValue;

	/**
	 * 异常类型
	 */
	private Integer abnormalType;

	/**
	 * 同比浮动比例
	 */
	private Double overLimitRadio;

	/**
	 * 同比历史值
	 */
	private Double historyValue;

	/**
	 * 最小值
	 */
	private Double minLimitValue;

	public AbnormalDetail(String topologyName, Long monitorTime, String indexName, Double indexValue){
		this.topologyName =topologyName;
		this.monitorTime =monitorTime;
		this.indexName =indexName;
		this.indexValue =indexValue;
	}

}

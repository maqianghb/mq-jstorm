package com.example.mq.jstorm.monitor.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import backtype.storm.generated.MetricInfo;
import backtype.storm.generated.TopologyInfo;
import backtype.storm.utils.NimbusClient;
import com.alibaba.fastjson.JSONObject;
import com.example.mq.jstorm.monitor.bean.MonitorTaskConfig;
import com.example.mq.jstorm.monitor.service.TaskDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.org.apache.commons.lang.StringUtils;

/**
 * @program: mq-jstorm
 * @description: jstorm集群获取运行数据
 * @author: maqiang
 * @create: 2019/3/15
 *
 */

public class JstormTaskDataService implements TaskDataService {
	private static Logger LOG = LoggerFactory.getLogger(JstormTaskDataService.class);

	@Override
	public Map<String, Double> queryTaskData(MonitorTaskConfig taskConfig, long monitorTime) throws Exception {
		if(null ==taskConfig){
			throw new IllegalArgumentException("参数为空！");
		}
		String clusterName =null;
		String topologyName =null;
		if(Objects.isNull(clusterName =taskConfig.getClusterName())
				|| Objects.isNull(topologyName =taskConfig.getTopologyName())){
			LOG.error("task配置数据不合规，taskConfig:{}", JSONObject.toJSONString(taskConfig));
			return null;
		}
		Map<String, String> metrics = null;
		try {
			metrics = this.queryTopologyData(clusterName, topologyName);
		} catch (Exception e) {
			LOG.error("query topology data err, clusterName:{}|topologyName:{}", clusterName, topologyName, e);
//			NimbusClientManager.removeClient(clusterName);
			metrics =this.queryTopologyData(clusterName, topologyName);
		}

		return null;
	}

	private Map<String, String> queryTopologyData(String clusterName, String topologyName) throws Exception{
		if(StringUtils.isEmpty(clusterName) || StringUtils.isEmpty(topologyName)){
			throw new IllegalArgumentException("参数为空！");
		}
//		NimbusClient client = NimbusClientManager.getNimbusClient(clusterName, 15 * 1000);
//		TopologyInfo topologyInfo = client.getClient().getTopologyInfo(client.getClient().getTopologyId(topologyName));
//		MetricInfo topologyMetric = topologyInfo.get_metrics().get_topologyMetric();
//		UISummaryMetric topologyData = UIMetricUtils.getSummaryMetrics(topologyMetric, UIUtils.parseWindow("60"));
//		return topologyData.getMetrics();
		return new HashMap<>();
	}
}

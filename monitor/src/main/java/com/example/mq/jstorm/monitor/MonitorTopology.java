package com.example.mq.jstorm.monitor;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseWindowedBolt;
import com.example.mq.jstorm.base.util.LocalPropertiesConfigurer;
import com.example.mq.jstorm.base.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2019/3/15
 *
 */

public class MonitorTopology {
	private static Logger LOG = LoggerFactory.getLogger(MonitorTopology.class);

	private static final String TOPOLOGY_NAME ="monitor-topology";
	private static final String SENTENCE_SPOUT_ID ="sentence-spout";
	private static final String SPLIT_BOLT_ID ="split-bolt";
	private static final String COUNT_BOLT_ID ="count-bolt";

	public static void main(String[] args) throws Exception{
		Long startTime =System.currentTimeMillis();
		//load properties
		Properties properties = null;
		try {
			LocalPropertiesConfigurer localPropertiesConfigurer = SpringContextUtil.getBean("localPropertiesConfigurer", LocalPropertiesConfigurer.class);
			properties = localPropertiesConfigurer.getLocalProperties();
		} catch (Exception e) {
			LOG.error("load properties err!", e);
			return;
		}
		if(Objects.isNull(properties)){
			LOG.error("properties is empty.");
			return;
		}

		//create and submit topology
		TopologyBuilder builder =createTopologyBuilder(properties);
		Config config =new Config();
		if( null !=args && args.length>0){
			LOG.info("begin submit topology to remoteCluster!");
			//线上集群模式
			config.setNumWorkers(2);
			config.setNumAckers(2);
			config.setMessageTimeoutSecs(3);
			config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 500);
			config.put(Config.TOPOLOGY_WORKER_MAX_HEAP_SIZE_MB, 1024);
			try {
				StormSubmitter.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());
			} catch (Exception e) {
				LOG.error("topology submit err, topologyName:{}", TOPOLOGY_NAME, e);
				return;
			}
		}else{
			LOG.info("begin submit topology to localCluster!");
			//本地模式
			LocalCluster cluster=new LocalCluster();
			try {
				cluster.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());
			} catch (Exception e) {
				LOG.error("topology submit err, topologyName:{}", TOPOLOGY_NAME, e);
				return;
			}
		}

		LOG.info("------start topology success, costTime:{}", System.currentTimeMillis()-startTime);
	}

	private static TopologyBuilder createTopologyBuilder(Properties properties){
		//窗口长度，5分钟
		BaseWindowedBolt.Duration windowLength=new BaseWindowedBolt.Duration(5 , TimeUnit.MINUTES);
		//滚动周期，1分钟
		BaseWindowedBolt.Duration slidingInterval=new BaseWindowedBolt.Duration(1 , TimeUnit.MINUTES);

		//拓扑构建
		TopologyBuilder builder =new TopologyBuilder();

		return builder;
	}
}

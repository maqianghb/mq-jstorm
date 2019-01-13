package com.example.mq.jstorm.wordcount.storm;



import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseWindowedBolt;
import backtype.storm.tuple.Fields;
import com.example.mq.jstorm.wordcount.util.MqJStormPlaceholderConfigurer;
import com.example.mq.jstorm.wordcount.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: mq-demo
 * @description: 单词统计拓扑
 * @author: maqiang
 * @create: 2018/9/4
 *
 */

public class WordCountTopology {
	private static Logger LOG = LoggerFactory.getLogger(WordCountTopology.class);

	private static final String TOPOLOGY_NAME ="word-count-topology";
	private static final String SENTENCE_SPOUT_ID ="sentence-spout";
	private static final String SPLIT_BOLT_ID ="split-bolt";
	private static final String COUNT_BOLT_ID ="count-bolt";

	public static void main(String[] args) throws Exception{
		Long startTime =System.currentTimeMillis();
		//load properties
		Properties properties = null;
		try {
			MqJStormPlaceholderConfigurer mqJStormConfigurer =SpringContextUtil.getBean("mqJStormConfigurer", MqJStormPlaceholderConfigurer.class);
			properties = mqJStormConfigurer.getMqJStormProperties();
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
		if( !Objects.isNull(args) && args.length>0){
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
		String sentenceStreamId ="sentenceStream";
		builder.setSpout(SENTENCE_SPOUT_ID, new SentenceSpout(properties, sentenceStreamId), 2)
				.setNumTasks(2);
		String wordStreamId ="workStream";
		builder.setBolt(SPLIT_BOLT_ID, new SplitSentenceBolt(properties, wordStreamId), 2)
				.setNumTasks(2)
				.fieldsGrouping(SENTENCE_SPOUT_ID, sentenceStreamId, new Fields("sentence"));
		builder.setBolt(COUNT_BOLT_ID, new WordCountBolt(properties), 2)
				.setNumTasks(2)
				.fieldsGrouping(SPLIT_BOLT_ID, wordStreamId, new Fields("word"));

		return builder;
	}
}

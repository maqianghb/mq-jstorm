package com.example.mq.jstorm.wordCount;



import java.util.Objects;
import java.util.concurrent.TimeUnit;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseWindowedBolt;
import backtype.storm.tuple.Fields;
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
	private static final String REPORT_BOLT_ID = "report-bolt";

	public static void main(String[] args) throws Exception{

		//窗口长度，5分钟
		BaseWindowedBolt.Duration windowLength=new BaseWindowedBolt.Duration(5 , TimeUnit.MINUTES);
		//滚动周期，1分钟
		BaseWindowedBolt.Duration slidingInterval=new BaseWindowedBolt.Duration(1 , TimeUnit.MINUTES);

		//拓扑构建
		TopologyBuilder builder =new TopologyBuilder();
		builder.setSpout(SENTENCE_SPOUT_ID, new SentenceSpout(), 1)
				.setNumTasks(1);

		builder.setBolt(SPLIT_BOLT_ID, new SplitSentenceBolt(), 1)
				.setNumTasks(1)
				.fieldsGrouping(SENTENCE_SPOUT_ID, new Fields("sentence"));
		builder.setBolt(COUNT_BOLT_ID, new WordCountBolt(), 2)
				.setNumTasks(1)
//				.fieldsGrouping(SPLIT_BOLT_ID, new Fields("word"));
				.globalGrouping(SPLIT_BOLT_ID);
//		builder.setBolt(REPORT_BOLT_ID, new ReportBolt(), 1)
//				.setNumTasks(1)
//				.globalGrouping(COUNT_BOLT_ID);

		Config config =new Config();
		config.setNumWorkers(2);
		config.put("wordsFile", "D:\\testFile\\test.txt");
		config.put("saveFilePath", "D:\\testFile\\saveTest.txt");
		config.setDebug(true);
		config.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);

		if( !Objects.isNull(args) && args.length>0){
			//线上集群模式
			config.setNumWorkers(4);
			config.setNumAckers(2);
			config.setMaxSpoutPending(1000);
			config.setMessageTimeoutSecs(480);
			try {
				StormSubmitter.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		}else{
			//本地模式
			LocalCluster cluster=new LocalCluster();
			cluster.submitTopology(TOPOLOGY_NAME,config,builder.createTopology());

			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cluster.killTopology(TOPOLOGY_NAME);
			cluster.shutdown();
		}

		System.out.println("--------start topology success！");

	}
}

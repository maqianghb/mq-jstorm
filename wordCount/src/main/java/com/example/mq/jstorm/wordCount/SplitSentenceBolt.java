package com.example.mq.jstorm.wordCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: mq-demo
 * @description: 分词bolt
 * @author: maqiang
 * @create: 2018/9/4
 *
 */

public class SplitSentenceBolt extends BaseRichBolt {
	private static final Logger LOG = LoggerFactory.getLogger(SplitSentenceBolt.class);

	private OutputCollector collector;


	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		LOG.info("------开始执行 SplitSentenceBolt.");
		this.collector =collector;
	}

	@Override
	public void execute(Tuple input) {
		String sentence = input.getStringByField("sentence");
		String[] words = sentence.split(" ");
		for(String word : words){
			this.collector.emit(new Values(word));
		}

		//确认成功处理一个tuple
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

		declarer.declare(new Fields("word"));
	}

}

package com.example.mq.jstorm.word.count.storm;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.org.apache.commons.lang.StringUtils;

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
	private String streamId;

	public SplitSentenceBolt(Properties properties, String streamId) {
		this.streamId = streamId;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector =collector;
	}

	@Override
	public void execute(Tuple input) {
		String sentence =null;
		if(StringUtils.isEmpty(sentence =input.getStringByField("sentence"))){
			LOG.warn("sentence is empty!");
			collector.ack(input);
			return;
		}
		String[] words = sentence.split(" ");
		if(Objects.isNull(words) || words.length ==0){
			collector.ack(input);
			return;
		}
		for(String word : words){
			this.collector.emit(streamId, new Values(word));
		}
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(streamId, new Fields("word"));
	}


}

package com.example.mq.jstorm.wordCount;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * @program: mq-jstorm
 * @description: 输出结果
 * @author: maqiang
 * @create: 2018/10/11
 *
 */

public class ReportBolt extends BaseRichBolt {

	private Map<String, Integer> counter =null;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.counter =new HashMap<>();
	}

	@Override
	public void execute(Tuple input) {
		String word =input.getStringByField("word");
		Integer count =input.getIntegerByField("count");
		this.counter.put(word, count);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	@Override
	public void cleanup() {
		System.out.println("--------统计结果--------");
		for(Map.Entry<String, Integer> entry :counter.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
		System.out.println("-------- end --------");
	}
}

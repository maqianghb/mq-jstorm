package com.example.mq.jstorm.wordCount;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: mq-demo
 * @description: 统计bolt
 * @author: maqiang
 * @create: 2018/9/4
 *
 */

public class WordCountBolt extends BaseRichBolt {
	private static final Logger LOG = LoggerFactory.getLogger(WordCountBolt.class);

	private OutputCollector collector;
	private HashMap<String, Integer> counter =null;
	private String saveFilePath;


	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		LOG.info("------开始执行 WordCountBolt.");
		this.collector =collector;
		this.counter =new HashMap<>();
		this.saveFilePath =stormConf.getOrDefault("saveFilePath", "D:\\testFile\\defaultSave.txt").toString();
	}

	@Override
	public void execute(Tuple input) {
		String word = input.getStringByField("word");
		Integer count = this.counter.get(word);
		count =Objects.isNull(count) ? 1:count+1;
		this.counter.put(word, count);
//		this.collector.emit(new Values(word, count));

		this.collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		declarer.declare(new Fields("word", "count"));
	}

	@Override
	public void cleanup() {
		System.out.println("--------统计结果--------");
		for(Map.Entry<String, Integer> entry :counter.entrySet()){
			try {
				String countInfo =entry.getKey()+":"+entry.getValue()+System.getProperty("line.separator");
				FileUtils.writeStringToFile(new File(saveFilePath), countInfo,
						Charset.forName("UTF-8"),true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

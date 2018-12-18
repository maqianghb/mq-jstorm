package com.example.mq.jstorm.wordCount.storm;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.org.apache.commons.lang.StringUtils;

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
	private Properties properties;
	private HashMap<String, Integer> counter =null;
	private String saveFilePath;

	public WordCountBolt(Properties properties){
		this.properties =properties;
	}


	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector =collector;
		this.counter =new HashMap<>();
		this.saveFilePath =properties.getProperty("word.count.jstorm.save.file");
	}

	@Override
	public void execute(Tuple input) {
		String word =null;
		if(StringUtils.isEmpty(word =input.getStringByField("word"))){
			LOG.warn("word is empty!");
			collector.ack(input);
			return;
		}
		if(counter.containsKey(word)){
			counter.put(word, counter.get(word)+1);
		}else{
			counter.put(word, 1);
		}
		LOG.info("------counter:{}", JSONObject.toJSONString(counter));
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public void cleanup() {
		System.out.println("------result:"+JSONObject.toJSONString(counter));
		for(Map.Entry<String, Integer> entry :counter.entrySet()){
			String countInfo =entry.getKey()+":"+entry.getValue()+System.getProperty("line.separator");
			try {
				FileUtils.writeStringToFile(new File(saveFilePath), countInfo, Charset.forName("UTF-8"), true);
			} catch (IOException e) {
				LOG.error("save result to file err!", e);
			}
		}
	}

}

package com.example.mq.jstorm.wordCount;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: mq-demo
 * @description: 数据流输入
 * @author: maqiang
 * @create: 2018/9/4
 *
 */

public class SentenceSpout extends BaseRichSpout {

	private static final long serialVersionUID = -8755428969225583517L;

	private static final Logger LOG = LoggerFactory.getLogger(SentenceSpout.class);

	private SpoutOutputCollector collector;
	private int maxLoop =1;
	private List<String> sentences =new ArrayList<>();


	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		LOG.info("------开始执行 SentenceSpout.");
		//初始化发射器
		this.collector = collector;
		try {
			//获取创建Topology时指定的要读取的文件路径
			FileReader fileReader = new FileReader(conf.get("wordsFile").toString());
			String str;
			BufferedReader reader = new BufferedReader(fileReader);
			while ((str = reader.readLine()) != null) {
				sentences.add(str);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading file ["
					+ conf.get("wordFile") + "]");
		}
	}

	@Override
	public void nextTuple() {
		for(int i=0; i<maxLoop; i++){
			for(String sentence :sentences){
				this.collector.emit(new Values(sentence));
			}
			//循环依次休眠10ms
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//循环依次休眠100ms
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

		declarer.declare(new Fields("sentence"));
	}

	@Override
	public void ack(Object msgId) {
		System.out.println("OK:" + msgId);
	}

	@Override
	public void fail(Object msgId) {
		System.out.println("FAIL:" + msgId);
	}
}

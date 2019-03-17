package com.example.mq.jstorm.wordcount.storm;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.example.mq.jstorm.base.util.SnowflakeIdWorker;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.org.apache.commons.lang.StringUtils;

/**
 * @program: mq-demo
 * @description: 数据流输入
 * @author: maqiang
 * @create: 2018/9/4
 *
 */

public class SentenceSpout extends BaseRichSpout {
	private static final Logger LOG = LoggerFactory.getLogger(SentenceSpout.class);
	private static final long serialVersionUID = -8755428969225583517L;

	private static final SnowflakeIdWorker idWorker =new SnowflakeIdWorker(5L, 6L);

	private SpoutOutputCollector collector;
	private Properties properties;
	private String streamId;
	private int maxLoop =100;
	private List<String> sentences =new ArrayList<>();

	public SentenceSpout(Properties properties, String streamId){
		this.properties =properties;
		this.streamId =streamId;
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		//读文件内容
		String wordsFile =null;
		if(StringUtils.isEmpty(wordsFile =properties.getProperty("word.count.jstorm.count.file"))){
			LOG.error("未获取到wordsFile地址！");
			return;
		}
		try {
			sentences =FileUtils.readLines(FileUtils.getFile(wordsFile), Charset.forName("UTF-8"));
		} catch (Exception e) {
			LOG.error("文件读取失败，wordsFile:{}", wordsFile, e);
		}
	}

	@Override
	public void nextTuple() {
		for(int i=0; i<maxLoop; i++){
			for(String sentence :sentences){
				Long messageId =idWorker.nextId();
				collector.emit(streamId, new Values(sentence), messageId);
			}
			//循环依次休眠10ms
			try {
				Thread.sleep(2*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(streamId, new Fields("sentence"));
	}

	@Override
	public void fail(Object msgId) {
		LOG.error("SentenceSpout do failed, msgId:{}", msgId);
		super.fail(msgId);
	}
}

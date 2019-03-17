package com.example.mq.jstorm.monitor.storm;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullSpout implements IRichSpout {
	private static final Logger LOG = LoggerFactory.getLogger(MonitorBolt.class);
	private SpoutOutputCollector outputCollector;
	private String streamId;

    public NullSpout(String streamId){
		this.streamId =streamId;
	}

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.outputCollector = collector;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream(streamId, new Fields("key","message"));
    }

    @Override
    public void close() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void nextTuple() {
        while (true){
            outputCollector.emit(streamId, new Values(System.currentTimeMillis(),"nullSpoutMessage"));
            try {
                //每次执行间隔30s
                Thread.sleep(30 * 1000);
            }catch (Exception e){
				LOG.error("thread sleep err!", e);
            }
        }
    }

    @Override
    public void ack(Object msgId) {

    }

    @Override
    public void fail(Object msgId) {

    }


    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}

package com.example.mq.jstorm.monitor.storm;

import java.util.Date;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.example.mq.jstorm.base.util.DateUtil;
import com.example.mq.jstorm.base.util.SpringContextUtil;
import com.example.mq.jstorm.monitor.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorBolt implements IRichBolt {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorBolt.class);
	private static final int TIME_LIMIT_MILLIS =200;

    protected OutputCollector collector;
    private String streamId;
    private MonitorService monitorService;

    public MonitorBolt(String streamId){
        this.streamId =streamId;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("result"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }


    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
		monitorService = SpringContextUtil.getBean(MonitorService.class);
		LOG.info("MonitorBolt prepare success");
    }
    @Override
    public void execute(Tuple input) {
		long monitorTime =System.currentTimeMillis();
		try {
			//do check
			monitorService.checkTaskAbnormalAndNotify(monitorTime);
		} catch (Exception e) {
			LOG.error("check task err, time:{}", DateUtil.formatDateTime(new Date(monitorTime)), e);
		}finally {
			this.collector.ack(input);
		}
		LOG.warn("check task cost {}ms, startTime:{}", System.currentTimeMillis()-monitorTime,
				DateUtil.formatDateTime(new Date(monitorTime)));
    }
    @Override
    public void cleanup() {

    }

   }

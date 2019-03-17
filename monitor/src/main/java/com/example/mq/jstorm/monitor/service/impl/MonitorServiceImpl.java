package com.example.mq.jstorm.monitor.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;

import com.example.mq.jstorm.base.mongo.MongoService;
import com.example.mq.jstorm.base.util.DateUtil;
import com.example.mq.jstorm.monitor.bean.AbnormalDetail;
import com.example.mq.jstorm.monitor.bean.MonitorDataRecord;
import com.example.mq.jstorm.monitor.bean.MonitorIndex;
import com.example.mq.jstorm.monitor.bean.MonitorTaskConfig;
import com.example.mq.jstorm.monitor.constant.MongoConstant;
import com.example.mq.jstorm.monitor.enums.TaskStatusEnum;
import com.example.mq.jstorm.monitor.service.AbnormalNotifyService;
import com.example.mq.jstorm.monitor.service.MonitorService;
import com.example.mq.jstorm.monitor.service.TaskConfigService;
import com.example.mq.jstorm.monitor.service.TaskDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/24
 *
 */
@Service
public class MonitorServiceImpl implements MonitorService {
	private static final Logger LOG = LoggerFactory.getLogger(MonitorServiceImpl.class);


	@Autowired
	private MongoConstant mongoConstant;

	@Autowired
	private MongoService mongoService;

	@Autowired
	TaskConfigService taskConfigService;

	@Autowired
	AbnormalNotifyService abnormalNotifyService;

	@Autowired
	TaskDataService jstormTaskDataService;


	@Override
	public void checkTaskAbnormalAndNotify(Long monitorTime) throws Exception{
		List<MonitorTaskConfig> taskConfigs = taskConfigService.getOpenMonitorTasks();
		if(CollectionUtils.isEmpty(taskConfigs)){
			return;
		}
		String timestamp = DateUtil.format(new SimpleDateFormat("yyyyMMddHHmm"), new Date(monitorTime));
		for(MonitorTaskConfig taskConfig : taskConfigs){
			//if not in monitor time section, skip check and do not save record
			if(!this.isInMonitorTimeSection(taskConfig, monitorTime)){
				continue;
			}
			Map<String, Object> options =new HashMap<>();
			options.put("taskId", taskConfig.getTaskId());
			options.put("monitorTimestamp", timestamp);
			if(mongoService.countByOptions(mongoConstant.MONITOR_TASK_DATA, options) >0){
				//the timestamp has record, then skip
				continue;
			}

			//create record and save
			MonitorDataRecord record =new MonitorDataRecord();
			record.setTaskId(taskConfig.getTaskId());
			record.setTaskName(taskConfig.getTaskName());
			record.setMonitorTimestamp(timestamp);
			Map<String, Double> taskRunDatas =jstormTaskDataService.queryTaskData(taskConfig, monitorTime);
			record.setTaskRunDatas(taskRunDatas);
			List<AbnormalDetail> abnormalDetails =this.checkAndGetAbnormalDetail(taskConfig, monitorTime, taskRunDatas);
			record.setStatus(CollectionUtils.isEmpty(abnormalDetails)
					? TaskStatusEnum.NORMAL.getCode() : TaskStatusEnum.ABNORMAL.getCode());
			record.setAbnormalDetails(CollectionUtils.isEmpty(abnormalDetails)
					? new ArrayList<>() : abnormalDetails );
			record.setCreateTime(System.currentTimeMillis());
			this.saveMonitorDataRecord(record);

			// task status is normal, skip notify
			if(TaskStatusEnum.NORMAL.getCode().equals(record.getStatus())){
				continue;
			}

			// if need notify, then send msg
			if(abnormalNotifyService.isNeedNotify(taskConfig.getTaskId(), monitorTime, taskConfig.getNotifyCondition())){
				LOG.warn("notify detail, taskConfig:{}|monitorTime:{}|abnormalDetails:{}",
						JSONObject.toJSONString(taskConfig), DateUtil.formatDateTime(new Date(monitorTime)), JSONObject.toJSONString(abnormalDetails));
				abnormalNotifyService.notify(taskConfig, monitorTime, abnormalDetails);
			}
		}

	}

	private List<AbnormalDetail> checkAndGetAbnormalDetail(MonitorTaskConfig taskConfig, Long monitorTime, Map<String, Double> taskRunDatas) throws Exception {
		return null;
	}

	private Boolean isInMonitorTimeSection(MonitorTaskConfig taskConfig, Long monitorTime) throws Exception {
		return false;
	}

	private double getHistoryValue(Long taskId, Long monitorTime, MonitorIndex monitorIndex)  throws Exception{
		return 0;
	}

	private void saveMonitorDataRecord(MonitorDataRecord record) throws Exception{
		if(Objects.isNull(record)){
			throw new IllegalArgumentException("参数为空！");
		}
		mongoService.insert(JSONObject.toJSONString(record), mongoConstant.MONITOR_TASK_DATA);
	}

}

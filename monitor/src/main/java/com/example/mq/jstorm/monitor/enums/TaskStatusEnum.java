package com.example.mq.jstorm.monitor.enums;

import java.util.Objects;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/26
 *
 */

public enum TaskStatusEnum {
	NORMAL(1, "正常状态"),
	ABNORMAL(2, "异常状态");

	private Integer code;
	private String desc;

	TaskStatusEnum(Integer code, String desc){
		this.code =code;
		this.desc =desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static TaskStatusEnum getByCode(Integer code){
		if(Objects.isNull(code)){
			return null;
		}
		for(TaskStatusEnum typeEnum : TaskStatusEnum.values()){
			if(typeEnum.getCode().equals(code)){
				return typeEnum;
			}
		}
		return null;
	}
}

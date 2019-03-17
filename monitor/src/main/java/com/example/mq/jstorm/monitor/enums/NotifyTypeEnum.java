package com.example.mq.jstorm.monitor.enums;

import java.util.Objects;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/25
 *
 */

public enum NotifyTypeEnum {
	SMS_MSG(1, "短信通知方式"),
	EMAIL(2, "邮件通知方式");

	private Integer code;
	private String desc;

	NotifyTypeEnum(Integer code, String desc){
		this.code =code;
		this.desc =desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static NotifyTypeEnum getByCode(Integer code){
		if(Objects.isNull(code)){
			return null;
		}
		for(NotifyTypeEnum typeEnum : NotifyTypeEnum.values()){
			if(typeEnum.getCode().equals(code)){
				return typeEnum;
			}
		}
		return null;
	}
}

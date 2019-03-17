package com.example.mq.jstorm.monitor.enums;

import java.util.Objects;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/28
 *
 */

public enum AbnormalTypeEnum {

	MIN_LIMIT(1, "低于最低值"),
	OVER_HISTORY_RADIO(2, "同比浮动过大");

	private Integer code;
	private String desc;

	AbnormalTypeEnum(Integer code, String desc){
		this.code =code;
		this.desc =desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static AbnormalTypeEnum getByCode(Integer code){
		if(Objects.isNull(code)){
			return null;
		}
		for(AbnormalTypeEnum typeEnum : AbnormalTypeEnum.values()){
			if(typeEnum.getCode().equals(code)){
				return typeEnum;
			}
		}
		return null;
	}
}

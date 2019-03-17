package com.example.mq.jstorm.monitor.enums;

import java.util.Objects;

/**
 * @program: mq-jstorm
 * @description: 同比类型
 * @author: maqiang
 * @create: 2018/12/29
 *
 */
public enum CompareTypeEnum {
	NOT_COMPARE_WITH_HISTORY(0, "不进行同比操作"),
	SAME_TIME_IN_YESTERDAY(1, "昨天同期值"),
	AVERAGE_OF_LATEST_TEN_MINUTE(2, "过去10分钟的均值"),
	AVERAGE_OF_LATEST_WEEK_ON_SAME_TIME(3, "过去一周同时期均值");

	private Integer code;
	private String desc;

	CompareTypeEnum(Integer code, String desc){
		this.code =code;
		this.desc =desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static CompareTypeEnum getByCode(Integer code){
		if(Objects.isNull(code)){
			return null;
		}
		for(CompareTypeEnum typeEnum : CompareTypeEnum.values()){
			if(typeEnum.getCode().equals(code)){
				return typeEnum;
			}
		}
		return null;
	}
}

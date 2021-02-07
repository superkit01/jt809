package com.superkit.jt809.enums;

import lombok.Getter;

/**
 * 报警类型
 */
@Getter
public enum AlarmTypeEnum {

	ALARM_OVERSPEED(0x0001, "超速报警"),
	ALARM_DRIVER_FATIGUE( 0x0002, "疲劳驾驶报警"),
	ALARM_EMERGENCY( 0x0003, "紧急报警"),
	ALARM_AREA_FORBID_IN( 0x0004, "禁入区域"),
	ALARM_AREA_FORBID_OUT( 0x0005, "禁出区域"),
	ALARM_ROAD_BLOCK_UP( 0x0006, "路段堵塞报警"),
	ALARM_ROAD_DANGEROUS( 0x0007, "危险路段报警"),
	ALARM_OUT_OF_BOUND( 0x0008, "越界报警"),
	ALARM_STEAL( 0x0009, "盗警"),
	ALARM_ROB( 0x000A, "劫警"),
	ALARM_LINE_OFFSET( 0x000B, "偏离路线报警"),
	ALARM_CAR_MOVE( 0x000C, "车辆移动报警"),
	ALARM_DRIVER_OVERTIME( 0x000D, "超时驾驶报警"),
	ALARM_OTHER( 0x000E, "其他")	;

	private int value;
	private String desc;

	AlarmTypeEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}


}

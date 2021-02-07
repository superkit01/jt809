package com.superkit.jt809.enums;

import lombok.Getter;

/**
 * 报警状态
 */
@Getter
public enum AlarmStatusEnum {

	NORMAL((byte) 0x01, "正常"), ALARM((byte) 0x02, "报警");

	private byte value;
	private String desc;

	AlarmStatusEnum(byte value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}

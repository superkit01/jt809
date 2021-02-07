package com.superkit.jt809.enums;

import lombok.Getter;

/**
 * 车牌颜色
 */
@Getter
public enum VehicleColorEnum {

	BLUE((byte) 0x01, "蓝色"), YELLOW((byte) 0x02, "黄色"), BLACK((byte) 0x03, "黑色"), WHITE((byte) 0x04, "白色"), OTHER((byte) 0x09, "其他");

	private byte value;
	private String desc;

	VehicleColorEnum(byte value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}

package com.superkit.jt809.enums;

import lombok.Getter;

/**
 * 报文加密标识位
 */
@Getter
public enum EncryptFlagEnum {

	NO((byte) 0, "不加密"), YES((byte) 1, "加密");

	private byte value;
	private String desc;

	EncryptFlagEnum(byte value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}

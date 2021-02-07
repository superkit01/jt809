package com.superkit.jt809.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class RabbitmqDto<T> {
	
	private int msgId; // 业务数据类型 byte[2]
	
	private int dataType; // 子业务类型标识 byte[2]
	
	private String vehicleNo;
	
	private byte vehicleColor;
	
	private T content;

}

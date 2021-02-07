package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 启动车辆定位信息交换请求消息 子业务类型标识:DOWN_EXG_MSG_RETURN_STARTUP
 * 描述:在有车辆进入非归属地区地理区域、人工指定车辆定位信息交换和应急状态监控车辆时，上级平台向下级平台发出启动车辆定位信息交换清求消息。下级平台收到此命令后
 * 需要回复启动车辆定位信息交换应答消息给上级平台，即UP_EXG_MSG_RETURN_STARTUP_ACK
 */
@Setter
@Getter
@NoArgsConstructor
public class ReturnStartupEntity implements Codec<ReturnStartupEntity> {
	private BaseExgMsgEntity baseMsg;

	/**
	 * 启动车辆定位信息交换的原因，定义如下： 0x00：车辆进入指定区域 0x01：人工指定交换 0x02：应急状态下车辆定位信息回传 0x03：其他原因
	 */
	private byte reasonCode; // 启动车辆定位信息交换的原因 byte[1]

	@Override
	public ReturnStartupEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.reasonCode = baseMsg.getData().getByte(0);
		return this;
	}

	public static ReturnStartupEntity create(String vehicleNo, byte vehicleColor, byte reasonCode) {
		ReturnStartupEntity entity = new ReturnStartupEntity();
		entity.reasonCode = reasonCode;
		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_RETURN_STARTUP, new byte[] { entity.reasonCode });
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 申请交换指定车辆定位信息应答消息 子业务类型标识:DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK
 * 描述:应答下级平台申请交换指定车辆定位信息,请求消息.即 UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplyForMonitorStartupAckEntity implements Codec<ApplyForMonitorStartupAckEntity> {
	private BaseExgMsgEntity baseMsg;

	/**
	 * 申请车辆定位信息交换的结果，定义如下： 0x00：申请成功 0x01：上级平台没有该车数据 0x02：申请时间段结束； 0x03：其他
	 */
	private byte result; // 申请车辆定位信息交换的结果 byte[1]

	@Override
	public ApplyForMonitorStartupAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.result = baseMsg.getData().getByte(0);
		return this;
	}

	public static ApplyForMonitorStartupAckEntity create(String vehicleNo, byte vehicleColor, byte result) {
		ApplyForMonitorStartupAckEntity entity = new ApplyForMonitorStartupAckEntity();
		entity.result = result;

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK, new byte[] { entity.result });
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

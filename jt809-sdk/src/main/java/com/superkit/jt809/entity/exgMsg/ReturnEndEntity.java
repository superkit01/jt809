package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 结束车辆定位信息交换请求消息 子业务类型标识:DOWN_EXG_MSG_RETURN_END
 * 描述:在进入非归属地区地理区域的车辆离开该地理区域、人工取消指定车辆定位信息交换和应急状态结束时，上级平台向下级平台发出结束车辆定位信息交换请求消息。下级平
 * 台收到该命令后应回复结束车辆定位信息交换应答消息，即 UP_EXG_MSG_RETURN_END_ACK
 */
@Setter
@Getter
@NoArgsConstructor
public class ReturnEndEntity implements Codec<ReturnEndEntity> {
	private BaseExgMsgEntity baseMsg;

	/**
	 * 结束车辆定位信息交换的原因，定义如下： 0x00：车辆离开指定区域 0x01：人工停止交换 0x02：紧急监控完成 0x03：其他原因
	 */
	private byte reasonCode; // 结束车辆定位信息交换的原因 byte[1]

	@Override
	public ReturnEndEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.reasonCode = baseMsg.getData().getByte(0);
		return this;
	}

	public static ReturnEndEntity create(String vehicleNo, byte vehicleColor, byte reasonCode) {
		ReturnEndEntity entity = new ReturnEndEntity();
		entity.reasonCode = reasonCode;
		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_RETURN_END, new byte[] { entity.reasonCode });
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

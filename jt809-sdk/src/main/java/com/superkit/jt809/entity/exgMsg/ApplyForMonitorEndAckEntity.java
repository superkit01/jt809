package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 取消申请交换指定车辆定位信息应答消息 子业务类型标识:DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK
 * 描述:应答下级平台取消申清交换指定车辆定位信息清求消息
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplyForMonitorEndAckEntity implements Codec<ApplyForMonitorEndAckEntity> {
	private BaseExgMsgEntity baseMsg;

	/**
	 * 取消申请车辆定位信息交换的结果，定义如下： 0x00：取消申请成功ExgMsgEntity 0x01：之前没有对应申请信息； 0x02：其他。
	 */
	private byte result; // 申请车辆定位信息交换的结果 byte[1]

	@Override
	public ApplyForMonitorEndAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.result = baseMsg.getData().getByte(0);
		return this;
	}
	
	public static ApplyForMonitorEndAckEntity create(String vehicleNo, byte vehicleColor, byte result) {
		ApplyForMonitorEndAckEntity entity = new ApplyForMonitorEndAckEntity();
		entity.result = result;

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK, new byte[] { entity.result });
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

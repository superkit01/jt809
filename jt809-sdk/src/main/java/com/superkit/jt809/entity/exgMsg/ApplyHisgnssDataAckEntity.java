package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 补发车辆定位信息应答消息 子业务类型标识:DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK
 * 描述:本条消息是上级平台应答下级平台发送的补发车辆定位信息请求消息,即UP_EXG_MSG_APPLY_HISGNSSDATA_REQ
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplyHisgnssDataAckEntity implements Codec<ApplyHisgnssDataAckEntity> {
	private BaseExgMsgEntity baseMsg;

	private byte result; // 补发车辆定位信息交换的原因 byte[1]

	@Override
	public ApplyHisgnssDataAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.result = baseMsg.getData().getByte(0);
		return this;
	}

	public static ApplyHisgnssDataAckEntity create(String vehicleNo, byte vehicleColor, byte result) {
		ApplyHisgnssDataAckEntity entity = new ApplyHisgnssDataAckEntity();
		entity.result = result;

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK, new byte[] { entity.result });
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

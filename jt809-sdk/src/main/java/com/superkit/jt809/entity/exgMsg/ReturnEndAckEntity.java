package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路 结束车辆定位信息交换应答消息 子业务类型标识:UP_EXG_MSG_RETURN_END_ACK
 * 描述:本条消息是下级平台对上级平台下发的 DOWN_EXG_MSG_RETURN_END 消息的应答消息
 */
@Setter
@Getter
@NoArgsConstructor
public class ReturnEndAckEntity implements Codec<ReturnEndAckEntity> {
	private BaseExgMsgEntity baseMsg;

	@Override
	public ReturnEndAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		return this;
	}

	public static ReturnEndAckEntity create(String vehicleNo, byte vehicleColor) {
		ReturnEndAckEntity entity = new ReturnEndAckEntity();

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_RETURN_END_ACK, new byte[0]);
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

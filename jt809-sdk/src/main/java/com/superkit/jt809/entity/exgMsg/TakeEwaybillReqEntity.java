package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 上报车辆电子运单请求消息 子业务类型标识:DOWN_EXG_MSG_TAKE_EWAYBILL_REQ
 * 描述:上级平台向下级平台下发上报车辆当前电子运单请求消息
 */
@Setter
@Getter
@NoArgsConstructor
public class TakeEwaybillReqEntity implements Codec<TakeEwaybillReqEntity> {
	private BaseExgMsgEntity baseMsg;

	@Override
	public TakeEwaybillReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		return this;
	}

	public static TakeEwaybillReqEntity create(String vehicleNo, byte vehicleColor) {
		TakeEwaybillReqEntity entity = new TakeEwaybillReqEntity();

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_TAKE_EWAYBILL_REQ, new byte[0]);
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

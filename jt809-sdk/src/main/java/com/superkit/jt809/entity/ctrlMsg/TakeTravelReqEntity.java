package com.superkit.jt809.entity.ctrlMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 上报车辆行驶记录请求消息 子业务类型标识:DOWN_CTRL_MSG_TAKE_TRAVEL_REQ
 * 描述:上级平台向下级平台下发上报车辆行驶记录请求消息
 */
@Setter
@Getter
@NoArgsConstructor
public class TakeTravelReqEntity implements Codec<TakeTravelReqEntity> {
	private BaseCtrlMsgEntity baseMsg;

	private long startTime; // 开始时间 byte[8]
	private long endTime; // 结束时间 byte[8]

	@Override
	public TakeTravelReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.startTime = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 8));
		this.endTime = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(8, 8));
		return this;
	}

	public static TakeTravelReqEntity create(String vehicleNo, byte vehicleColor, long startTime, long endTime) {
		TakeTravelReqEntity entity = new TakeTravelReqEntity();
		entity.startTime = startTime;
		entity.endTime = endTime;

		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_CTRL_MSG_TAKE_TRAVEL_REQ,
				ByteUtil.combineByteArray(ByteUtil.long2Bytes(entity.startTime), ByteUtil.long2Bytes(entity.endTime)));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

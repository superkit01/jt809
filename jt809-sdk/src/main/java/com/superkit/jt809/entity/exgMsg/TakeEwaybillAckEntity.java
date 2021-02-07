package com.superkit.jt809.entity.exgMsg;

import java.nio.charset.Charset;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路 上报车辆电子运单应答消息 子业务类型标识:UP_EXG_MSG_TAKE_EWAYBILL_ACK
 * 描述:下级平台应答上级平台发送的上报车辆电子运单请求消息，向上级平台上传车辆当前电子运单
 */
@Setter
@Getter
@NoArgsConstructor
public class TakeEwaybillAckEntity implements Codec<TakeEwaybillAckEntity> {
	private BaseExgMsgEntity baseMsg;

	private long ewaybillLength; // 电子运单数据体长度 byte[4]
	private String ewaybillInfo; // 电子运单数据内容 byte[ewaybillLength]

	@Override
	public TakeEwaybillAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.ewaybillLength = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.ewaybillInfo = new String(
				BufUtil.bufToBytes(baseMsg.getData().slice(4, baseMsg.getData().readableBytes() - 4)),
				Charset.forName("GBK"));
		return this;
	}

	public static TakeEwaybillAckEntity create(String vehicleNo, byte vehicleColor, String ewaybillInfo) {
		TakeEwaybillAckEntity entity = new TakeEwaybillAckEntity();
		entity.ewaybillInfo = ewaybillInfo;

		byte[] ewaybillInfoBytes = ewaybillInfo.getBytes(Charset.forName("GBK"));

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_TAKE_EWAYBILL_ACK,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) ewaybillInfoBytes.length), ewaybillInfoBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

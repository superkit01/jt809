package com.superkit.jt809.entity.ctrlMsg;

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
 * 链路类型:主链路 上报车辆行驶记录应答消息 子业务类型标识:UP_CTRL_MSG_TAKE_TRAVEL_ACK
 * 描述:下级平台应答上级平台下发的上报车辆行驶记录请求消息，将车辆行驶记录数据上传至上级平台
 */
@Setter
@Getter
@NoArgsConstructor
public class TakeTravelAckEntity implements Codec<TakeTravelAckEntity> {
	private BaseCtrlMsgEntity baseMsg;

	private long travelDataLength; // 车辆行驶记录数据体长度 byte[4]

	private String travelDataInfo; // 车辆行驶记录信息 byte[travelDataLength]

	@Override
	public TakeTravelAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.travelDataLength = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.travelDataInfo = new String(
				BufUtil.bufToBytes(baseMsg.getData().slice(4, baseMsg.getData().readableBytes() - 4)),
				Charset.forName("GBK"));
		return this;
	}

	public static TakeTravelAckEntity create(String vehicleNo, byte vehicleColor, String travelDataInfo) {
		TakeTravelAckEntity entity = new TakeTravelAckEntity();
		byte[] travelDataInfoBytes = travelDataInfo.getBytes(Charset.forName("GBK"));
		entity.travelDataLength = travelDataInfoBytes.length;
		entity.travelDataInfo = travelDataInfo;

		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_CTRL_MSG_TAKE_TRAVEL_ACK,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) entity.travelDataLength), travelDataInfoBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

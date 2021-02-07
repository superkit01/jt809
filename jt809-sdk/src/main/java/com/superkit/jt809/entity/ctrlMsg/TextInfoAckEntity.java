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
 * 链路类型:主链路 下发车辆报文应答消息 子业务类型标识:UP_CTRL_MSG_TEXT_INFO_ACK
 * 描述:下级平台应答上级平台下发的报文是否成功到达指定车辆
 */
@Setter
@Getter
@NoArgsConstructor
public class TextInfoAckEntity implements Codec<TextInfoAckEntity> {
	private BaseCtrlMsgEntity baseMsg;

	private long msgSequence; // 对应“下发车辆报文请求消息”中的MSG_ID byte[4]
	/**
	 * 下发车辆报文应答结果，定义如下： 0x00：下发成功 0x01：下发失败
	 */
	private byte result; // 下发车辆报文应答结果 byte[1]

	@Override
	public TextInfoAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.msgSequence = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.result = baseMsg.getData().getByte(4);
		return this;
	}

	public static TextInfoAckEntity create(String vehicleNo, byte vehicleColor, long msgSequence, byte result) {
		TextInfoAckEntity entity = new TextInfoAckEntity();
		entity.msgSequence = msgSequence;
		entity.result = result;

		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_CTRL_MSG_TEXT_INFO_ACK,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) entity.msgSequence), new byte[] { entity.result }));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}
}

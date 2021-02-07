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
 * 链路类型:从链路 下发车辆报文请求消息 子业务类型标识:DOWN_CTRL_MSG_TEXT_INFO_REQ
 * 描述:用于上级平台向下级平台下发报文到某指定车辆
 */
@Setter
@Getter
@NoArgsConstructor
public class TextInfoReqEntity implements Codec<TextInfoReqEntity> {
	private BaseCtrlMsgEntity baseMsg;

	private long msgSequence; // 消息ID序号，本序号作为标识本消息的唯一标识 byte[4]
	/**
	 * 报文优先级，定义如下： 0x00：紧急 0x01：一般
	 */
	private byte msgPriority; // 报文优先级， byte[1]
	private long messageLength; // 报文信息长度，最长1024字节 byte[4]
	private String msgContent; // 报文信息内容 byte[msgLength]


	@Override
	public TextInfoReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.msgSequence = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.msgPriority = baseMsg.getData().getByte(4);
		this.messageLength = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(5, 4));
		this.msgContent = new String(
				BufUtil.bufToBytes(baseMsg.getData().slice(9, baseMsg.getData().readableBytes() - 9)),
				Charset.forName("GBK"));
		return this;
	}

	public static TextInfoReqEntity create(String vehicleNo, byte vehicleColor, long msgSequence, byte msgPriority,
			String msgContent) {
		TextInfoReqEntity entity = new TextInfoReqEntity();
		entity.msgSequence = msgSequence;
		entity.msgPriority = msgPriority;
		entity.msgContent = msgContent;

		byte[] msgContentBytes = msgContent.getBytes(Charset.forName("GBK"));
		entity.messageLength = msgContentBytes.length;

		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_CTRL_MSG_TEXT_INFO_REQ,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) entity.msgSequence),
						new byte[] { entity.msgPriority }, ByteUtil.int2Byte((int) entity.messageLength),
						msgContentBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

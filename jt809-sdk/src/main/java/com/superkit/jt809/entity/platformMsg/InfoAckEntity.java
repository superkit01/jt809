package com.superkit.jt809.entity.platformMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路 下发平台间报文应答消息 子业务类型标识:UP_PLATFORM_MSG_INFO_ACK
 * 描述:下级平台收到上级平台发送的下发平台间报文请求消息后，发送应答消息
 */
@Setter
@Getter
@NoArgsConstructor
public class InfoAckEntity implements Codec<InfoAckEntity> {
	private BasePlatFormMsgEntity baseMsg;

	private long infoId; // 信息ID，本ID跟下发的ID相同 byte[4]

	@Override
	public InfoAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BasePlatFormMsgEntity().decode(buf);
		this.infoId = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		return this;
	}

	public static InfoAckEntity create(long infoId) {
		InfoAckEntity entity = new InfoAckEntity();
		entity.infoId = infoId;

		entity.baseMsg = BasePlatFormMsgEntity.create(MsgConstant.SubServiceType.UP_PLATFORM_MSG_INFO_ACK,
				ByteUtil.int2Byte((int) infoId));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

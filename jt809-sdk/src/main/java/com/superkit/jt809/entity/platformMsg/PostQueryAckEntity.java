package com.superkit.jt809.entity.platformMsg;

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
 * 链路类型:主链路 平台查岗应答消息 子业务类型标识:UP_PLATFORM_MSG_POST_QUERY_ACK
 * 描述:下级平台应答上级平台发送的不定期平台查岗消息
 */
@Setter
@Getter
@NoArgsConstructor
public class PostQueryAckEntity implements Codec<PostQueryAckEntity> {
	private BasePlatFormMsgEntity baseMsg;

	private long infoId; // 信息ID，本ID跟下发的ID相同 byte[4]
	private long infoLength; // 数据长度 byte[4]
	private String infoContent; // 应答内容 byte[infoLength]

	@Override
	public PostQueryAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BasePlatFormMsgEntity().decode(buf);
		this.infoId = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.infoLength = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(4, 4));
		this.infoContent = new String(
				BufUtil.bufToBytes(baseMsg.getData().slice(8, baseMsg.getData().readableBytes() - 8)));
		return this;
	}

	public static PostQueryAckEntity create(long infoId, String infoContent) {
		PostQueryAckEntity entity = new PostQueryAckEntity();
		entity.infoId = infoId;
		byte[] infoContentBytes = infoContent.getBytes(Charset.forName("GBK"));
		entity.infoLength = infoContentBytes.length;
		entity.infoContent = infoContent;

		entity.baseMsg = BasePlatFormMsgEntity.create(MsgConstant.SubServiceType.UP_PLATFORM_MSG_POST_QUERY_ACK,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) entity.infoId),
						ByteUtil.int2Byte((int) entity.infoLength), infoContentBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

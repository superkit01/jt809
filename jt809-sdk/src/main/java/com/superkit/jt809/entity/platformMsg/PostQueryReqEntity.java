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
 * 链路类型:从链路 平台查岗请求消息 子业务类型标识:DOWN_PLATFORM_MSG_POST_QUERY_REQ
 * 描述:上级平台不定期向下级平台发送平台查岗信息
 */
@Setter
@Getter
@NoArgsConstructor
public class PostQueryReqEntity implements Codec<PostQueryReqEntity> {
	private BasePlatFormMsgEntity baseMsg;

	private long infoId; // 信息ID，本ID跟下发的ID相同 byte[4]
	private long infoLength; // 数据长度 byte[4]
	private String infoContent; // 应答内容 byte[infoLength]

	@Override
	public PostQueryReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BasePlatFormMsgEntity().decode(buf);
		this.infoId = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.infoLength = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(4, 4));
		this.infoContent = new String(
				BufUtil.bufToBytes(baseMsg.getData().slice(8, baseMsg.getData().readableBytes() - 8)));
		return this;
	}

	public static PostQueryReqEntity create(long infoId, String infoContent) {
		PostQueryReqEntity entity = new PostQueryReqEntity();
		entity.infoId = infoId;
		byte[] infoContentBytes = infoContent.getBytes(Charset.forName("GBK"));
		entity.infoLength = infoContentBytes.length;
		entity.infoContent = infoContent;

		entity.baseMsg = BasePlatFormMsgEntity.create(MsgConstant.SubServiceType.DOWN_PLATFORM_MSG_POST_QUERY_REQ,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) entity.infoId),
						ByteUtil.int2Byte((int) entity.infoLength), infoContentBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

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
 * 链路类型:从链路 下发平台间报文请求消息 子业务类型标识:DOWN_PLATFORM_MSG_INFO_REQ
 * 描述:上级平台不定期向下级平台下发平台间报文
 */
@Setter
@Getter
@NoArgsConstructor
public class InfoReqEntity implements Codec<InfoReqEntity> {
	private BasePlatFormMsgEntity baseMsg;

	private long infoId; // 信息ID byte[4]
	private long infoLength; // 数据长度 byte[4]
	private String infoContent; // 应答内容 byte[infoLength]

	@Override
	public InfoReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BasePlatFormMsgEntity().decode(buf);
		this.infoId = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.infoLength = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(4, 4));
		this.infoContent = new String(
				BufUtil.bufToBytes(baseMsg.getData().slice(8, baseMsg.getData().readableBytes() - 8)));
		return this;
	}

	public static InfoReqEntity create(long infoId, String infoContent) {
		InfoReqEntity entity = new InfoReqEntity();
		entity.infoId = infoId;
		byte[] infoContentBytes = infoContent.getBytes(Charset.forName("GBK"));
		entity.infoLength = infoContentBytes.length;
		entity.infoContent = infoContent;

		entity.baseMsg = BasePlatFormMsgEntity.create(MsgConstant.SubServiceType.DOWN_PLATFORM_MSG_INFO_REQ,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) entity.infoId),
						ByteUtil.int2Byte((int) entity.infoLength), infoContentBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

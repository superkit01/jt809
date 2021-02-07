package com.superkit.jt809.entity;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.enums.EncryptFlagEnum;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;
import com.superkit.jt809.util.JT809Util;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseEntity implements Codec<BaseEntity> {

	/**
	 * |头标识|消息头|消息体|校验码|尾标识|
	 */
	private byte headFlag = MsgConstant.HEAD_FLAG; // 头标识 byte

	private Header header; // 数据头 byte[22]

	private ByteBuf messageBody; // 解密后的数据体

	private byte[] crcCode;// CRC校验码 byte[2]

	private byte tailFlag = MsgConstant.TAIL_FLAG;;// 尾标识 byte

	@Override
	public BaseEntity decode(ByteBuf buf) {
		this.headFlag = buf.readByte();
		ByteBuf decodedBuf = JT809Util.decode(buf.slice(1, buf.readableBytes() - 2));
		this.header = new Header().decode(decodedBuf.slice(0, 22));

		ByteBuf bodyTmp = decodedBuf.slice(22, decodedBuf.readableBytes() - 3 - 22);
		this.messageBody = this.header.getEncryptFlag() == EncryptFlagEnum.YES.getValue()
				? BufUtil.bytesToBuf(JT809Util.encrypt(this.header.getEncryptKey(), BufUtil.bufToBytes(bodyTmp)))
				: bodyTmp;

		this.crcCode = BufUtil.bufToBytes(decodedBuf.slice(decodedBuf.readableBytes() - 3, 2));
		return this;
	}

	public static BaseEntity create(int msgId, byte[] msgbody) {
		BaseEntity base = new BaseEntity();
		base.header = Header.create(msgId, msgbody.length);
		base.messageBody = BufUtil.bytesToBuf(msgbody);
		base.crcCode = JT809Util.createCrcCode(base.header, msgbody);
		return base;
	}

	@Override
	public byte[] encode() {
		byte[] tmpBody = header.getEncryptFlag() == EncryptFlagEnum.YES.getValue()
				? JT809Util.encrypt(header.getEncryptKey(), BufUtil.bufToBytes(messageBody))
				: BufUtil.bufToBytes(messageBody);

		byte[] encodedBytes = JT809Util.encode(ByteUtil.combineByteArray(header.encode(), tmpBody, crcCode));

		return ByteUtil.combineByteArray(new byte[] { headFlag }, encodedBytes, new byte[] { tailFlag });
	}

}

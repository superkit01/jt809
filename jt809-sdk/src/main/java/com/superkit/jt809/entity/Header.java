package com.superkit.jt809.entity;

import com.superkit.jt809.JT809Config;
import com.superkit.jt809.enums.EncryptFlagEnum;
import com.superkit.jt809.util.JT809Util;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Header implements Codec<Header>{
	/**
	 * 消息头长度
	 */
	public static final int MSG_HEADER_LENTH = 22;

	private long msgLength; // 数据长度(包括头标识、数据头、数据体、校验码和尾标识) byte[4]

	private long msgSn; // 报文序列号 byte[4]

	private int msgId; // 业务数据类型 byte[2]

	private long msgGnssscenterId; // 下级平台接入码，上级平台给下级平台分配唯一标识码 byte[4]

	private byte[] versionFlag = new byte[3]; // 协议版本号标识，长度为 3 个字节来表示，0x01 0x02 0x0F标识的版本号是v1.2.15 byte[3]

	private byte encryptFlag = EncryptFlagEnum.NO.getValue(); // 报文加密标识位 0 表示报文不加密，1 表示报文加密 byte[1]

	private long encryptKey = 0L; // 数据加密的密匙，长度为 4 个字节。 byte[4]

	@Override
	public  Header decode(ByteBuf buf) {
		Header header = new Header();
		header.msgLength = BufUtil.buf2UnsignedLong(buf.slice(1, 4));
		header.msgSn = BufUtil.buf2UnsignedLong(buf.slice(5, 4));
		header.msgId = BufUtil.buf2UnsignedInt(buf.slice(9, 2));
		header.msgGnssscenterId = BufUtil.buf2UnsignedLong(buf.slice(11, 4));
		header.versionFlag = BufUtil.bufToBytes(buf.slice(15, 3));
		header.encryptFlag = buf.getByte(18);
		header.encryptKey = BufUtil.buf2UnsignedLong(buf.slice(19, 4));
		return header;
	}

	/**
	 * 创建消息头
	 */
	public static Header create(int msgId,int msgbodyLength) {
		Header header = new Header();
		header.msgLength = 1 + Header.MSG_HEADER_LENTH + msgbodyLength + 2 + 1;
		header.msgSn=JT809Util.getMsgSn();
		header.msgId=msgId;
		header.msgGnssscenterId=JT809Config.msmsgGnssscenterId;
		header.versionFlag=JT809Config.VERSION_FLAG;
		return header;
	}
	

	@Override
	public byte[]  encode() {
		byte[] head = new byte[MSG_HEADER_LENTH];
		System.arraycopy(ByteUtil.int2Byte((int) msgLength), 0, head, 0, 4);
		System.arraycopy(ByteUtil.int2Byte((int) msgSn), 0, head, 4, 4);
		System.arraycopy(ByteUtil.short2Bytes((short) msgId), 0, head, 8, 2);
		System.arraycopy(ByteUtil.int2Byte((int) msgGnssscenterId), 0, head, 10, 4);
		System.arraycopy(versionFlag, 0, head, 14, 3);
		head[17] = encryptFlag;
		System.arraycopy(ByteUtil.int2Byte((int) encryptKey), 0, head, 18, 4);
		return head;
	}

}


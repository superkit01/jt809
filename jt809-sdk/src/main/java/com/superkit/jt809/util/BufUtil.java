package com.superkit.jt809.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class BufUtil {

	/**
	 * bytebuf合并
	 */
	public static ByteBuf compositeByteBufs(ByteBuf... bufs) {
		return Unpooled.wrappedBuffer(bufs);
	}

	public static byte[] bufToBytes(ByteBuf buf) {

		byte[] bytes = new byte[buf.readableBytes()];
		buf.getBytes(0, bytes);
		return bytes;
	}

	public static ByteBuf bytesToBuf(byte[] bytes) {
		return Unpooled.wrappedBuffer(bytes);
	}

	/**
	 * ByteBuf 转 十六进制字符串
	 * 
	 */
	public static String buf2HexStr(ByteBuf buf) {
		if (buf == null || buf.readableBytes() <= 0) {
			return null;
		}
		return ByteBufUtil.hexDump(buf);
	}

	/**
	 * 十六进制字符串 转 ByteBuf
	 * 
	 */
	public static ByteBuf hexStr2Buf(String hexStr) {

		if (hexStr.length() % 2 != 0) {
			throw new IllegalArgumentException(hexStr);
		}

		return Unpooled.wrappedBuffer(ByteUtil.hex2Bytes(hexStr));
	}

	public static long buf2UnsignedLong(ByteBuf buf) {

		if (buf.readableBytes() != 4) {
			throw new RuntimeException("只有4个字节才能转为无符号long");
		}

		int s0 = buf.getByte(0) & 0xff;
		int s1 = buf.getByte(1) & 0xff;
		int s2 = buf.getByte(2) & 0xff;
		int s3 = buf.getByte(3) & 0xff; // 最低位
		s0 <<= 24;
		s1 <<= 16;
		s2 <<= 8;
		int val = s0 | s1 | s2 | s3;

		return ByteUtil.getUnsignedLong(val);

	}

	public static long buf2SignedLong(ByteBuf buf) {

		if (buf.readableBytes() != 8) {
			throw new RuntimeException("只有8个字节才能转为有符号long");
		}

		int s0 = buf.getByte(0) & 0xff;
		int s1 = buf.getByte(1) & 0xff;
		int s2 = buf.getByte(2) & 0xff;
		int s3 = buf.getByte(3) & 0xff;
		int s4 = buf.getByte(4) & 0xff;
		int s5 = buf.getByte(5) & 0xff;
		int s6 = buf.getByte(6) & 0xff;
		int s7 = buf.getByte(7) & 0xff;
		s0 <<= 56;
		s1 <<= 48;
		s2 <<= 40;
		s3 <<= 32;
		s4 <<= 24;
		s5 <<= 16;
		s6 <<= 8;
		long val = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;

		return val;

	}

	public static int buf2UnsignedInt(ByteBuf buf) {
		if (buf.readableBytes() != 2) {
			throw new RuntimeException("只有2个字节才能转为无符号int");
		}

		short temp = (short) (((buf.getByte(0) & 0xff) << 8) | (buf.getByte(1) & 0xff));
		return ByteUtil.getUnsignedInt(temp);
	}

}

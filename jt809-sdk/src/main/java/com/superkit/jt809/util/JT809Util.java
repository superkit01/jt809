package com.superkit.jt809.util;

import java.util.concurrent.atomic.AtomicLong;

import com.superkit.jt809.JT809Config;
import com.superkit.jt809.entity.Header;

import io.netty.buffer.ByteBuf;

public class JT809Util {

	private static volatile AtomicLong msgSnNo = new AtomicLong(0);

	/**
	 * 获取流水号
	 *
	 */
	public static long getMsgSn() {
		if (msgSnNo.get() >= Long.MAX_VALUE || msgSnNo.get() <= 0) {
			msgSnNo.set(0x00000001);
			return 0x00000001L;
		} else {
			long msgSn = msgSnNo.incrementAndGet();
			return msgSn;
		}
	}

	/**
	 * CRC 校验码
	 *
	 */
	public static byte[] getCRC(byte[] bytes) {

		int crc = 0xFFFF;
		for (int j = 0; j < bytes.length; j++) {
			crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
			crc ^= (bytes[j] & 0xff);
			crc ^= ((crc & 0xff) >> 4);
			crc ^= (crc << 12) & 0xffff;
			crc ^= ((crc & 0xFF) << 5) & 0xffff;
		}
		crc &= 0xffff;
		return ByteUtil.short2Bytes((short) crc);
	}

	/**
	 * 计算CRC
	 */
	public static byte[] createCrcCode(Header header, byte[] msgbody) {
		byte[] crc = getCRC(ByteUtil.combineByteArray(header.encode(), msgbody));
		return crc;

	}

	private static final Long UINT32_MAX_VALUE = 4294967295L;

	/**
	 * 消息体加密,解密
	 */
	public static byte[] encrypt(long key, byte[] data) {

		if (!JT809Config.encrypt) {
			throw new RuntimeException("M1 IA1 IC1 not initialized ,msgody can not encrypted");
		}

		if (data == null)
			return null;

		int idx = 0;
		if (key == 0) {
			key = 1;
		}
		long mkey = JT809Config.M1;
		if (0 == mkey) {
			mkey = 1;
		}
		while (idx < data.length) {
			key = JT809Config.IA1 * (key % mkey) + JT809Config.IC1;
			if (key > UINT32_MAX_VALUE) {
				key &= UINT32_MAX_VALUE;
			}
			data[idx++] ^= (byte) ((key >> 20) & 0xFF);
		}
		return data;
	}

	/**
	 * 头标识为字符 0x5b 尾标识为字符 0x5d 数据内容进行转义判断，转义规则如下: a) 若数据内容中有出现字符 0x5b 的，需替换为字符 0x5a
	 * 紧跟字符 0x01 b) 若数据内容中有出现字符 0x5a 的，需替换为字符 0x5a 紧跟字符 0x02 c) 若数据内容中有出现字符 0x5d
	 * 的，需替换为字符 0x5e 紧跟字符 0x01 d) 若数据内容中有出现字符 0x5e 的，需替换为字符 0x5e 紧跟字符 0x02
	 */

	/**
	 * 还原
	 */
	public static byte[] decode(byte[] b) {

		int newlen = b.length;

		for (int i = 1; i < (b.length - 1); i++) {
			if (b[i] == 0x5a || b[i] == 0x5e) {
				newlen--;
			}
		}

		byte[] newb = new byte[newlen];

		newb[0] = b[0];
		newb[newlen - 1] = b[b.length - 1];

		for (int i = 1, j = 1; i < (b.length - 1); i++, j++) {
			if (b[i] == 0x5a && b[i + 1] == 0x01) {
				newb[j] = 0x5b;
				i++;
			} else if (b[i] == 0x5a && b[i + 1] == 0x02) {
				newb[j] = 0x5a;
				i++;
			} else if (b[i] == 0x5e && b[i + 1] == 0x01) {
				newb[j] = 0x5d;
				i++;
			} else if (b[i] == 0x5e && b[i + 1] == 0x02) {
				newb[j] = 0x5e;
				i++;
			} else {
				newb[j] = b[i];
			}
		}
		return newb;
	}

	/**
	 * 转义
	 */
	public static byte[] encode(byte[] b) {

		int newlen = b.length;

		for (int i = 1; i < (b.length - 1); i++) {
			if (b[i] == 0x5b || b[i] == 0x5a || b[i] == 0x5d || b[i] == 0x5e) {
				newlen++;
			}
		}

		byte[] newb = new byte[newlen];

		newb[0] = b[0];
		newb[newlen - 1] = b[b.length - 1];

		for (int i = 1, j = 1; i < (b.length - 1); i++, j++) {
			if (b[i] == 0x5b) {
				newb[j] = 0x5a;
				newb[++j] = 0x01;
			} else if (b[i] == 0x5a) {
				newb[j] = 0x5a;
				newb[++j] = 0x02;
			} else if (b[i] == 0x5d) {
				newb[j] = 0x5e;
				newb[++j] = 0x01;
			} else if (b[i] == 0x5e) {
				newb[j] = 0x5e;
				newb[++j] = 0x02;
			} else {
				newb[j] = b[i];
			}
		}
		return newb;
	}

	/**
	 * 转义
	 */
	public static ByteBuf encode(ByteBuf buf) {
		byte[] initBytes = BufUtil.bufToBytes(buf);
		return BufUtil.bytesToBuf(encode(initBytes));
	}

	/**
	 * 转义还原
	 */
	public static ByteBuf decode(ByteBuf buf) {
		byte[] initBytes = BufUtil.bufToBytes(buf);
		return BufUtil.bytesToBuf(decode(initBytes));
	}

}

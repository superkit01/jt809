package com.superkit.jt809.util;

import java.math.BigInteger;

public final class ByteUtil {


    /**
     * 把byte转为字符串的bit
     */
    public static String byte2Bit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String bytes2Bit(byte[] bytes) {
        StringBuilder bits = new StringBuilder("");
        for (int i = 0; i < bytes.length; i++) {
            bits.append(byte2Bit(bytes[i]));
        }
        return bits.toString();
    }

    /**
     * 从十六进制字符串到字节数组转换
     */
    public static byte[] hex2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    /**
     * byte转16进制String
     *
     * @param b
     * @return
     */
    public static String byte2Hex(byte b) {
        int v = b & 0xFF;
        String hs = Integer.toHexString(v);
        if (hs.length() < 2) {
            return "0" + hs;
        } else {
            return hs;
        }
    }

    /**
     * bytes转16进制String
     *
     * @param src
     * @return
     */
    public static String bytes2Hex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            stringBuilder.append(byte2Hex(src[i]));
        }
        return stringBuilder.toString();
    }

    /**
     * 将int转化成byte[]
     *
     * @param res
     * @return
     */
    public static byte[] int2Byte(int res) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (res & 0xff);// 最低位
        targets[2] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[1] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[0] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }


    public static long byte2UnsignedLong(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff; // 最低位
        s0 <<= 24;
        s1 <<= 16;
        s2 <<= 8;
        s = s0 | s1 | s2 | s3;
        return getUnsignedLong(s);
    }

    public static long byte2SignedLong(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff; // 最低位
        s0 <<= 24;
        s1 <<= 16;
        s2 <<= 8;
        s = s0 | s1 | s2 | s3;
        return (long) s;
    }

    public static byte[] long2Bytes(long res) {
        byte[] targets = new byte[8];
        targets[7] = (byte) (res & 0xff);// 最低位
        targets[6] = (byte) ((res >> 8) & 0xff);
        targets[5] = (byte) ((res >> 16) & 0xff);
        targets[4] = (byte) ((res >> 24) & 0xff);
        targets[3] = (byte) ((res >> 32) & 0xff);
        targets[2] = (byte) ((res >> 40) & 0xff);
        targets[1] = (byte) ((res >> 48) & 0xff);
        targets[0] = (byte) (res >>> 56);// 最高位,无符号右移。
        return targets;
    }


    /**
     * 将short转化成byte[]
     *
     * @param res
     * @return
     */
    public static byte[] short2Bytes(short res) {
        byte[] targets = new byte[2];
        targets[1] = (byte) (res & 0xff);// 低位
        targets[0] = (byte) (res >> 8);// 高位,无符号右移。
        return targets;
    }

    /**
     * 将byte[]转化成short
     *
     * @param b
     * @return
     */
    public static int byte2UnsignedInt(byte[] b) {
        short temp = (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
        return getUnsignedInt(temp);
    }

    public static int byte2SignedInt(byte[] b) {
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
    }

    /*
     * 解析BCD码成十进制,x为BCD码表示
     */
    public static byte bcd2Byte(byte b) {
        int xx = getUnsignedByte(b);
        StringBuilder ss = new StringBuilder("");
        while (xx > 0) {
            ss.insert(0, xx % 2);
            xx = (byte) (xx / 2);
        }
        while (ss.length() < 8) {
            ss.insert(0, '0');
        }
        String s = ss.toString();
        String s_gao = s.substring(0, 4);
        String s_di = s.substring(4, 8);
        BigInteger src_di = new BigInteger(s_di, 2);
        int r_di = new Integer(src_di.toString()).intValue();

        BigInteger src_gao = new BigInteger(s_gao, 2);// 转换为BigInteger类型
        int r_gao = new Integer(src_gao.toString()).intValue();
        byte r = (byte) (r_gao * 10 + r_di);
        return r;
    }

    /**
     * 将byte字节型数据转换为0~255 (0xFF 即BYTE)
     *
     * @param data
     * @return
     */
    public static int getUnsignedByte(byte data) {
        return data & 0x0FF;
    }

    /**
     * 将short字节型数据转换为0~65535 (0xFFFF 即 WORD)
     *
     * @param data
     * @return
     */
    public static int getUnsignedInt(short data) {
        return data & 0x0FFFF;
    }

    /**
     * 将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)
     *
     * @return
     */
    public static long getUnsignedLong(int data) {
        return data & 0x0FFFFFFFFL;
    }

    public static byte[] combineByteArray(byte[]... data) {
        int length = 0;
        for (int i = 0; i < data.length; i++) {
            length += data[i].length;
        }

        byte[] result = new byte[length];
        int position = 0;
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, result, position, data[i].length);
            position += data[i].length;
        }

        return result;

    }
 
}

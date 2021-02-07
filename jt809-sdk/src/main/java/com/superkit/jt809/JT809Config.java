package com.superkit.jt809;

import java.util.Objects;

public class JT809Config {
	private static final String DEFAULT_VERSIION = "1.2.15";
	public static byte[] VERSION_FLAG;

	public static int M1;
	public static int IA1;
	public static int IC1;

	public static boolean encrypt = false;

	public static long userId;
	public static String password;
	public static String localIp;
	public static Integer localPort;
	public static long msmsgGnssscenterId;

	public static void init(long userId, String password, String localIp, Integer localPort, long msgGnssscenterId) {
		init(userId, password, localIp, localPort, msgGnssscenterId, DEFAULT_VERSIION);
	}

	public static void init(long userId, String password, String localIp, Integer localPort, long msgGnssscenterId,
			String version) {
		init(userId, password, localIp, localPort, msgGnssscenterId, version, null, null, null);
	}

	public static void init(long userId, String password, String localIp, Integer localPort, long msgGnssscenterId,
			String version, Integer M1, Integer IA1, Integer IC1) {
		JT809Config.userId = userId;
		JT809Config.password = password;
		JT809Config.localIp = localIp;
		JT809Config.localPort = localPort;
		JT809Config.msmsgGnssscenterId = msgGnssscenterId;

		initVersion(version);

		if (Objects.nonNull(M1) && Objects.nonNull(IA1) && Objects.nonNull(IC1)) {
			encrypt = true;
			JT809Config.M1 = M1;
			JT809Config.IA1 = IA1;
			JT809Config.IC1 = IC1;
		}
	}

	private static void initVersion(String version) {
		String[] tmp = version.split(".");
		if (tmp.length > 3) {
			throw new IllegalArgumentException("version参数有误 eg:1.2.15");
		}

		for (int i = 0; i < tmp.length; i++) {
			if (isNumeric(tmp[i])) {
				throw new IllegalArgumentException("version参数有误 eg:1.2.15");
			}
		}

		byte[] versionBytes = new byte[3];
		for (int i = 0; i < tmp.length; i++) {
			versionBytes[i] = Byte.parseByte(tmp[i]);
		}
		JT809Config.VERSION_FLAG = versionBytes;
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}

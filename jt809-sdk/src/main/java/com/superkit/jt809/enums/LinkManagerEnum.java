package com.superkit.jt809.enums;

import lombok.Getter;

public class LinkManagerEnum {
	/**
	 * 主链路登陆应答结果
	 */
	@Getter
	public enum MainConnectRspEnum {

	SUCCESS((byte) 0x00, "成功"), WRONG_IP((byte) 0x01, "IP 地址不正确"), WRONG_GNSSSID((byte) 0x02, "接入码不正确"), USER_NOT_REGIST((byte) 0x03, "用户没有注册"), WRONG_PASSWORD((byte) 0x04, "密码错误"), NON_AVAILABLE_RESOURCE((byte) 0x05, "资源紧张，稍后再连接(已经占用)"), OTHER((byte) 0x06, "其他");

		private byte value;
		private String desc;

		MainConnectRspEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 主链路断开通知错误码
	 */
	@Getter
	public enum MainDisConnectInformEnum {

		MAIN_LINK_CLOSED((byte) 0x00, "主链路断开"), OTHER((byte) 0x01, "其他原因");

		private byte value;
		private String desc;

		MainDisConnectInformEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 从链路登陆应答结果
	 */
	@Getter
	public enum SubConnectRspEnum {

		SUCCESS((byte) 0x00, "成功"), WRONG_VERIFY_CODE((byte) 0x01, "VERIFY_CODE 错误"), NON_AVAILABLE_RESOURCE((byte) 0x02, "资源紧张，稍后再连接（已经占用）"), OTHER((byte) 0x03, "其他");

		private byte value;
		private String desc;

		SubConnectRspEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 从链路断开通知错误码
	 */
	@Getter
	public enum SubDisConnectInformEnum {

		WRONG_IP_OR_PORT((byte) 0x00, "无法连接下级平台指定的服务 IP 与端口"), LINK_CLOSED((byte) 0x01, "上级平台客户端与下级平台服务端断开"), OTHER((byte) 0x02, "其他原因");

		private byte value;
		private String desc;

		SubDisConnectInformEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

}

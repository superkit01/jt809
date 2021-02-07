package com.superkit.jt809.enums;

import lombok.Getter;

public class WarnMsgEnum {
	/**
	 * 督办级别
	 */
	@Getter
	public enum SupervisionLevelEnum {

	URGE((byte) 0x00, "紧急"), NORMAL((byte) 0x01, "一般");

		private byte value;
		private String desc;

		SupervisionLevelEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 报警督办应答结果
	 */
	@Getter
	public enum UrgeTodoResultEnum {

		DOING((byte) 0x00, "处理中"), FINISHED((byte) 0x01, "已处理完毕"), WONT_TO_DO((byte) 0x02, "不做处理"), WILL_TO_DO((byte) 0x03, "将来处理");

		private byte value;
		private String desc;

		UrgeTodoResultEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 报警信息来源
	 */
	@Getter
	public enum WarnSrcEnum {

		TERMINAL_IN_CAR((byte) 0x00, "车载终端"), ENTERPRISE_PLATFORM((byte) 0x01, "企业监控平台"), GOVERNMENT_PLATFORM((byte) 0x02, "政府监管平台"), OTHER((byte) 0x09, "其他");

		private byte value;
		private String desc;

		WarnSrcEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}
}
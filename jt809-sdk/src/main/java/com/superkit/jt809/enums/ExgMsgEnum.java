package com.superkit.jt809.enums;

import lombok.Getter;

public class ExgMsgEnum {
	/**
	 * 申请车辆定位信息交换结果
	 */
	@Getter
	public enum ApplyForMonitorStartupReasonEnum {

	SUCCESS((byte) 0x00, "申请成功"), NO_CAR((byte) 0x01, "上级平台没有该车数据"), PERIED_FINISH((byte) 0x02, "申请时间段结束"), OTHER((byte) 0x03, "其他");

		private byte value;
		private String desc;

		ApplyForMonitorStartupReasonEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 补发车辆定位信息交换结果
	 */
	@Getter
	public enum ApplyHisgnssDataAckEnum {

		SUCCESS_NOW((byte) 0x00, "成功，上级平台即刻补发"), SUCCESS_MONMENT((byte) 0x01, "成功，上级平台择机补发"), FAIL_NO_GNSSDATA((byte) 0x02, "失败，上级平台无对应申请的定位数据"), FAIL_WRONG_APLLYINFO((byte) 0x03, "失败，申请内容不正确"), OTHER((byte) 0x04, "其他原因");

		private byte value;
		private String desc;

		ApplyHisgnssDataAckEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 结束车辆定位信息交换的原因
	 */
	@Getter
	public enum ReturnEndReasonEnum {

		CAR_EXIT_AREA((byte) 0x00, "车辆离开指定区域"), MANUAL_STOP((byte) 0x01, "人工停止交换"), EMERGENCY_FINISHED((byte) 0x02, "紧急监控完成"), OTHER((byte) 0x03, "其他原因");

		private byte value;
		private String desc;

		ReturnEndReasonEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 启动车辆定位信息交换的原因
	 */
	@Getter
	public enum ReturnStartupReasonEnum {

		CAR_ENTER_AREA((byte) 0x00, "车辆进入指定区域"), MANUAL_DESIGNATED((byte) 0x01, "人工指定交换"), EMERGENCY_RETURN((byte) 0x02, "应急状态下车辆定位信息回传"), OTHER((byte) 0x03, "其他原因");

		private byte value;
		private String desc;

		ReturnStartupReasonEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}
}
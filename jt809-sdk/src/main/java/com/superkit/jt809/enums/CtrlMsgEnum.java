package com.superkit.jt809.enums;

import lombok.Getter;

public class CtrlMsgEnum {
	/**
	 * 车辆应急接入监管平台结果
	 *
	 */
	@Getter
	public enum EmergencyMonitoringEnum {

	TERMINAL_RECEIVED((byte) 0x00, "车载终端成功收到命令"), NO_CAR((byte) 0x01, "车辆不存在"), OTHER((byte) 0x02, "其他原因失败");

		private byte value;
		private String desc;

		EmergencyMonitoringEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 车辆单向监听应答结果
	 */
	@Getter
	public enum MonitorVehicleEnum {

		SUCCESS((byte) 0x00, "监听成功"), FAIL((byte) 0x01, "监听失败");

		private byte value;
		private String desc;

		MonitorVehicleEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 车辆拍照图片格式
	 */
	@Getter
	public enum TakePhotoFormatTypeEnum {

		JPG((byte) 0x01, "jpg"), GIF((byte) 0x02, "gif"), TIFF((byte) 0x03, "tiff"), PNG((byte) 0x04, "png");

		private byte value;
		private String desc;

		TakePhotoFormatTypeEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 图片大小
	 */
	@Getter
	public enum TakePhotoSizeTypeEnum {

		S1((byte) 0x01, "320x240"), S2((byte) 0x02, "640x480"), S3((byte) 0x03, "800x600"), S4((byte) 0x04, "1024x768"), S5((byte) 0x05, "l76x144"), S6((byte) 0x06, "352*288"), S7((byte) 0x07, "704*288"), S8((byte) 0x08, "704*576");

		private byte value;
		private String desc;

		TakePhotoSizeTypeEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 拍照应答标识
	 */
	@Getter
	public enum TakePhotoRspEnum {

		NOT_SUPPORTED((byte) 0x00, "不支持拍照"), SUCCESS((byte) 0x01, "完成拍照"), SUCCESS_MONMENT((byte) 0x02, "完成拍照、照片数据稍后传送"), NOT_ONLINE((byte) 0x03, "未拍照(不在线)"), CAMERA_NOT_AVALIABLE((byte) 0x04, "未拍照;(无法使用指定镜头)"), OTHER((byte) 0x05, "未拍照(其他原因)"), WRONG_VEHICLE_NO((byte) 0x09, "车牌号码错误");

		private byte value;
		private String desc;

		TakePhotoRspEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 报文优先级
	 */
	@Getter
	public enum PriorityEnum {

		URGE((byte) 0x00, "紧急"), NORMAL((byte) 0x01, "一般");

		private byte value;
		private String desc;

		PriorityEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}

	/**
	 * 下发车辆报文应答结果
	 */
	@Getter
	public enum TextInfoResultEnum {

		SUCCESS((byte) 0x01, "下发成功"), FAIL((byte) 0x02, "下发失败");

		private byte value;
		private String desc;

		TextInfoResultEnum(byte value, String desc) {
			this.value = value;
			this.desc = desc;
		}
	}
}

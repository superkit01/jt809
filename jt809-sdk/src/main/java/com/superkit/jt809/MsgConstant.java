package com.superkit.jt809;

public class MsgConstant {
	/**
	 * 头标识
	 */
	public static final byte HEAD_FLAG = 0x5b;
	/**
	 * 尾标识
	 */
	public static final byte TAIL_FLAG = 0x5d;

	/**
	 * 主业务类型的业务标识
	 * <p>
	 * 业务数据类型标识的命名规则如下: a) 上级平台向下级平台发送的请求消息，一般以“DOWN_”开头，以后缀_REQ 结尾;
	 * 而下级平台向上级平台发送的请求消息一般以“UP_”开头，以后缀_REQ 结尾; b)
	 * 当上下级平台之间有应答消息情况下，应答消息可继续沿用对应的请求消息开头标识符，而通过后缀 RSP 来标识结尾。
	 */
	public static class PrimaryServiceType {

		/**
		 * 链路管理类
		 */
		public static final int UP_CONNECT_REQ = 0x1001;// 主链路--主链路登录请求消息
		public static final int UP_CONNECT_RSP = 0x1002;// 主链路--主链路登录应答消息
		public static final int UP_DISCONNECT_REQ = 0x1003;// 主链路--主链路注销请求消息
		public static final int UP_DISCONNECT_RSP = 0x1004;// 主链路--主链路注销应答消息
		public static final int UP_LINKTEST_REQ = 0x1005;// 主链路--主链路连接保持请求消息
		public static final int UP_LINKTEST_RSP = 0x1006;// 主链路--主链路连接保持应答消息
		public static final int UP_DISCONNECT_INFORM = 0x1007;// 从链路--主链路断开通知消息
		public static final int UP_CLOSELINK_INFORM = 0x1008;// 从链路--下级平台主动关闭链路通知消息
		public static final int DOWN_CONNECT_REQ = 0x9001;// 从链路--从链路连接请求消息从链路
		public static final int DOWN_CONNECT_RSP = 0x9002;// 从链路--从链路连接应答消息
		public static final int DOWN_DISCONNECT_REQ = 0x9003;// 从链路--从链路注销请求消息
		public static final int DOWN_DISCONNECT_RSP = 0x9004;// 从链路--从链路注销应答消息

		public static final int DOWN_LINKTEST_REQ = 0x9005;// 从链路--从链路连接保持请求消息
		public static final int DOWN_LINKTEST_RSP = 0x9006;// 从链路--从链路连接保持应答消息
		public static final int DOWN_DISCONNECT_INFORM = 0x9007;// 主链路--从链路断开通知消息
		public static final int DOWN_CLOSELINK_INFORM = 0x9008;// 主链路--上级平台主动关闭链路通知消息

		/**
		 * 信息统计类
		 */
		public static final int DOWN_TOTAL_RECY_BACK_MSG = 0x9101;// 从链路--接收定位信息数量通知消息

		/**
		 * 车辆动态信息交换
		 */
		public static final int UP_EXG_MSG = 0x1200;// 主链路--主链路动态信息交换消息
		public static final int DOWN_EXG_MSG = 0x9200;// 从链路--从链路动态信息交换消息

		/**
		 * 平台间信息交互类
		 */
		public static final int UP_PLATFORM_MSG = 0x1300;// 主链路--主链路平台间信息交互消息
		public static final int DOWN_PLATFORM_MSG = 0x9300;// 从链路--从链路平台间信息交互消息

		/**
		 * 车辆报警信息交互类
		 */
		public static final int UP_WARN_MSG = 0x1400;// 主链路--主链路报警信息交互消息
		public static final int DOWN_WARN_MSG = 0x9400;// 从链路--从链路报警信息交互消息

		/**
		 * 车辆监管类
		 */
		public static final int UP_CTRL_MSG = 0x1500;// 主链路--主链路车辆监管消息
		public static final int DOWN_CTRL_MSG = 0x9500;// 从链路--从链路车辆监管消息

		/**
		 * 车辆静态信息交换类
		 */
		public static final int UP_BASE_MSG = 0x1600;// 主链路--主链路静态信息交换消息
		public static final int DOWN_BASE_MSG = 0x9600;// 从链路--从链路静态信息交换消息


	}

	/**
	 * 子业务类型的业务标识
	 * <p>
	 * 子业务类型标识命名规则女如下: a)对应于业务数据类型下的子业务标识头继续遵循原有归属业务数据类型的标识头，例如业务数据类型 UP_EXG_MSG
	 * 下的子业务类型标识头均以“UP_EXG_MSG”开始; b)子业务类型名称标识的主从链路方向遵循原有归属业务数据类型的主从链路方向。
	 */
	public static class SubServiceType {
		/**
		 * 主链路动态信息交换消息 UP_EXG_MSG
		 */
		public static final int UP_EXG_MSG_REGISTER = 0x1201;// 上传车辆注册信息
		public static final int UP_EXG_MSG_REAL_LOCATION = 0x1202;// 实时上传车辆定位信息
		public static final int UP_EXG_MSG_HISTORY_LOCATION = 0x1203;// 车辆定位信息自动补报
		public static final int UP_EXG_MSG_RETURN_STARTUP_ACK = 0x1205;// 启动车辆定位信息交换应答
		public static final int UP_EXG_MSG_RETURN_END_ACK = 0x1206;// 结束车辆定位信息交换应答
		public static final int UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP = 0x1207;// 申请交换指定车辆定位信息请求
		public static final int UP_EXG_MSG_APPLY_FOR_MONITOR_END = 0x1208;// 取消交换指定车辆定位信息请求
		public static final int UP_EXG_MSG_APPLY_HISGNSSDATA_REQ = 0x1209;// 补发车辆定位信息请求
		public static final int UP_EXG_MSG_REPORT_DRIVER_INFO_ACK = 0x120A;// 上报车辆驾驶员身份识别信息应答
		public static final int UP_EXG_MSG_TAKE_EWAYBILL_ACK = 0x120B;// 上报车辆电子运单应答

		/**
		 * 从链路动态信息交换消息 DOWN_EXG_MSG
		 */
		public static final int DOWN_EXG_MSG_CAR_LOCATION = 0x9202;// 交换车辆定位信息
		public static final int DOWN_EXG_MSG_HISTORY_ARCOSSAREA = 0x9203;// 车辆定位信息交换补发
		public static final int DOWN_EXG_MSG_CAR_INFO = 0x9204;// 交换车辆静态信息
		public static final int DOWN_EXG_MSG_RETURN_STARTUP = 0x9205;// 启动车辆定位信息交换请求
		public static final int DOWN_EXG_MSG_RETURN_END = 0x9206;// 结束车辆定位信息交换请求
		public static final int DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK = 0x9207;// 申请交换指定车辆定位应答
		public static final int DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK = 0x9208;// 取消交换指定车辆定位应答
		public static final int DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK = 0x9209;// 补发车辆定位信息应答
		public static final int DOWN_EXG_MSG_REPORT_DRIVER_INFO = 0x920A;// 上报车辆驾驶员身份识别信息请求
		public static final int DOWN_EXG_MSG_TAKE_EWAYBILL_REQ = 0x920B;// 上报车辆电子运单请求

		/**
		 * 主链路平台信息交互消息 UP_PLATFORM_MSG
		 */
		public static final int UP_PLATFORM_MSG_POST_QUERY_ACK = 0x1301;// 平台查岗应答
		public static final int UP_PLATFORM_MSG_INFO_ACK = 0x1302;// 下发平台间报文应答

		/**
		 * 从链路平台信息交互消息 DOWN_PLATFORM_MSG
		 */
		public static final int DOWN_PLATFORM_MSG_POST_QUERY_REQ = 0x9301;// 平台查岗应答
		public static final int DOWN_PLATFORM_MSG_INFO_REQ = 0x9302;// 下发平台间报文应答

		/**
		 * 主链路报警信息交互消息 UP_WARN_MSG
		 */
		public static final int UP_WARN_MSG_URGE_TODO_ACK = 0x1401;// 报警督办应答
		public static final int UP_WARN_MSG_ADPT_INFO = 0x1402;// 上报报警信息

		/**
		 * 从链路报警信息交互消息 DOWN_WARN_MSG
		 */
		public static final int DOWN_WARN_MSG_URGE_TODO_REQ = 0x9401;// 报警督办请求
		public static final int DOWN_WARN_MSG_INFORM_TIPS = 0x9402;// 报警预警
		public static final int DOWN_WARN_MSG_EXG_INFORM = 0x9403;// 实时交换报警消息

		/**
		 * 主链路车辆监管消息 UP_CTRL_MSG
		 */
		public static final int UP_CTRL_MSG_MONITOR_VEHICLE_ACK = 0x1501;// 车辆单向监听应答
		public static final int UP_CTRL_MSG_TAKE_PHOTO_ACK = 0x1502;// 车辆拍照应答
		public static final int UP_CTRL_MSG_TEXT_INFO_ACK = 0x1503;// 下发车辆行驶记录应答
		public static final int UP_CTRL_MSG_TAKE_TRAVEL_ACK = 0x1504;// 上报车辆行驶记录应答
		public static final int UP_CTRL_MSG_EMERGENCY_MONITORING_ACK = 0x1505;// 车辆应急接入监管平台应答消息

		/**
		 * 从链路车辆监管消息 DOWN_CTRL_MSG
		 */
		public static final int DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ = 0x9501;// 车辆单向监听请求
		public static final int DOWN_CTRL_MSG_TAKE_PHOTO_REQ = 0x9502;// 车辆拍照请求
		public static final int DOWN_CTRL_MSG_TEXT_INFO_REQ = 0x9503;// 下发车辆报文请求
		public static final int DOWN_CTRL_MSG_TAKE_TRAVEL_REQ = 0x9504;// 上报车辆行驶记录应答
		public static final int DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ = 0x9505;// 车辆应急接入监管平台请求消息

		/**
		 * 主链路静态信息交换消息 UP_BASE_MSG
		 */
		public static final int UP_BASE_MSG_VEHICLE_ADDED_ACK = 0x1601;// 补报车辆静态信息应答

		/**
		 * 从链路静态信息交换消息 DOWN_BASE_MSG
		 */
		public static final int DOWN_BASE_MSG_VEHICLE_ADDED = 0x9601;// 补报车辆静态信息请求



	}




}

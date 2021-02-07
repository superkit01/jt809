package com.superkit.jt809.processor;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.superkit.jt809.entity.exgMsg.ApplyForMonitorEndAckEntity;
import com.superkit.jt809.entity.exgMsg.ApplyForMonitorEndEntity;
import com.superkit.jt809.entity.exgMsg.ApplyForMonitorStartupAckEntity;
import com.superkit.jt809.entity.exgMsg.ApplyForMonitorStartupEntity;
import com.superkit.jt809.entity.exgMsg.ApplyHisgnssDataAckEntity;
import com.superkit.jt809.entity.exgMsg.ApplyHisgnssDataReqEntity;
import com.superkit.jt809.entity.exgMsg.CarInfoEntity;
import com.superkit.jt809.entity.exgMsg.CarLocationEntity;
import com.superkit.jt809.entity.exgMsg.HistoryArcossareaEntity;
import com.superkit.jt809.entity.exgMsg.HistoryLocationEntity;
import com.superkit.jt809.entity.exgMsg.RealLocationEntity;
import com.superkit.jt809.entity.exgMsg.RegisterEntity;
import com.superkit.jt809.entity.exgMsg.ReportDriverInfoAckEntity;
import com.superkit.jt809.entity.exgMsg.ReportDriverInfoEntity;
import com.superkit.jt809.entity.exgMsg.ReturnEndAckEntity;
import com.superkit.jt809.entity.exgMsg.ReturnEndEntity;
import com.superkit.jt809.entity.exgMsg.ReturnStartupAckEntity;
import com.superkit.jt809.entity.exgMsg.ReturnStartupEntity;
import com.superkit.jt809.entity.exgMsg.TakeEwaybillAckEntity;
import com.superkit.jt809.entity.exgMsg.TakeEwaybillReqEntity;
import com.superkit.jt809.manager.ChannelManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态信息交换业务
 */
@Slf4j
public class ExgProcessor {

	private RabbitTemplate rabbitTemplate;

	public ExgProcessor(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	/********************* * 主链路车辆动态信息交换业务 **************/
	/**
	 * 上传车辆注册信息消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_EXG_MSG_REGISTER
	 * 描述:监控平台收到车载终端鉴权信息后，启动本命令向上级监管平台上传该车辆注册信息.各级监管平台再逐级向上级平台上传该信息
	 */
	public void sendExgMsgRegister(String vehicleNo, byte vehicleColor, String plateFormId, String producerId,
			String terminalModelType, String terminalId, String terminalSimCode) {
		log.info("发送车辆注册信息消息");

		ChannelManager.sendMsgWithMainLinkPrefer(RegisterEntity.create(vehicleNo, vehicleColor, plateFormId, producerId,
				terminalModelType, terminalId, terminalSimCode).encode());
	}

	/**
	 * 车辆定位信息自动补报请求消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_EXG_MSG_HISTORY_LOCATION
	 * 描述:如果平台间传输链路中断，下级平台重新登录并与上级平台建立通信链路后，下级平台应将中断期间内车载终端上传的车辆定位信息自动补报到上级平台。如果系统断线期间，
	 * 该车需发送的数据包条数大于5，则以每包五条进行补发，直到补发完毕。多条数据以卫星定位时间先后顺序排列。本条消息上级平台采用定量回复，即收到一定数量的数据后，即通过从链路应答数据量
	 */
	public void sendExgMsgHistoryLocation(Channel channel, String vehicleNo, byte vehicleColor, List<byte[]> gnssData) {
		log.info("发送车辆定位信息自动补报请求消息");

		ChannelManager
				.sendMsgWithMainLinkPrefer(HistoryLocationEntity.create(vehicleNo, vehicleColor, gnssData).encode());
	}

	/**
	 * 启动车辆定位信息交换应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_EXG_MSG_RETURN_STARTUP_ACK
	 * 描述:本条消息是下级平台对上级平台下发的 DOWN_EXG_MSG_RETURN_STARTUP 消息的应答消息
	 */
	public void sendExgMsgReturnStartupAck(String vehicleNo, byte vehicleColor) {
		log.info("发送启动车辆定位信息交换应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(ReturnStartupAckEntity.create(vehicleNo, vehicleColor).encode());
	}

	/**
	 * 结束车辆定位信息交换应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_EXG_MSG_RETURN_END_ACK
	 * 描述:本条消息是下级平台对上级平台下发的 DOWN_EXG_MSG_RETURN_END 消息的应答消息
	 */
	public void sendExgMsgReturnEndAck(String vehicleNo, byte vehicleColor) {
		log.info("发送结束车辆定位信息交换应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(ReturnEndAckEntity.create(vehicleNo, vehicleColor).encode());
	}

	/**
	 * 申请交换指定车辆定位信息请求消息 链路类型:主链路 消息方向:下级平台往上级平台
	 * 子业务类型标识:UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP
	 * 描述:当下级平台需要在特定时问段内监控特殊车辆时，可上传此命令到上级平台申请对该车辆定位数据交换到下级平台，申请成功后，此车辆定位数据将在指定时间内交换到该平台(即使该车没有进入该平台所属区域也会交换)
	 */
	public void sendExgMsgApplyForMonitorStartup(String vehicleNo, byte vehicleColor, long startTime, long endTime) {
		log.info("发送申请交换指定车辆定位信息请求消息");

		ChannelManager.sendMsgWithMainLinkPrefer(
				ApplyForMonitorStartupEntity.create(vehicleNo, vehicleColor, startTime, endTime).encode());

	}

	/**
	 * 取消交换指定车辆定位信息请求 链路类型:主链路 消息方向:下级平台往上级平台
	 * 子业务类型标识:UP_EXG_MSG_APPLY_F0R_MONITOR_END 描述:下级平台上传该命令给上级平台，取消之前申请监控的特殊车辆
	 */
	public void sendExgMsgApplyForMonitorEnd(String vehicleNo, byte vehicleColor) {
		log.info("发送取消交换指定车辆定位信息请求");
		ChannelManager.sendMsgWithMainLinkPrefer(ApplyForMonitorEndEntity.create(vehicleNo, vehicleColor).encode());
	}

	/**
	 * 补发车辆定位信息请求消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_EXG_MSG_APPLY_HISGNSSDATA_REQ
	 * 描述:在平台间传输链路中断并重新建立连接后，下级平台向上级平台请求中断期间内上级平台需交换至下级平台的车辆定位信息时，向上级平台发出补发车辆定位信息请求，上级平台对请求应答后进行“补发车辆定位信息”
	 */
	public void sendExgMsgApplyHisgnssDataReq(String vehicleNo, byte vehicleColor, long startTime, long endTime) {
		log.info("发送补发车辆定位信息请求消息");

		ChannelManager.sendMsgWithMainLinkPrefer(
				ApplyHisgnssDataReqEntity.create(vehicleNo, vehicleColor, startTime, endTime).encode());
	}

	/**
	 * 上报驾驶员身份识别信息应答消息 链路类型:主链路 消息方向:下级平台往上级平台
	 * 子业务类型标识:UP_EXG_MSG_REPORT_DRIVER_INFO_ACK
	 * 描述:下级平台应答上级平台发送的上报驾驶员身份识别信息请求消息，上传指定车辆的驾驶员身份识别信息数据
	 */
	public void sendExgMsgReportDriverInfoAck(String vehicleNo, byte vehicleColor, String driverName, String driverId,
			String licence, String orgName) {
		log.info("发送上报驾驶员身份识别信息应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(ReportDriverInfoAckEntity
				.create(vehicleNo, vehicleColor, driverName, driverId, licence, orgName).encode());
	}

	/**
	 * 上报车辆电子运单应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_EXG_MSG_TAKE_EWAYBILL_ACK
	 * 描述:下级平台应答上级平台发送的上报车辆电子运单请求消息，向上级平台上传车辆当前电子运单
	 */
	public void sendExgMsgTakeEwaybillAck(String vehicleNo, byte vehicleColor, String ewaybillInfo) {
		log.info("发送上报车辆电子运单应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(
				TakeEwaybillAckEntity.create(vehicleNo, vehicleColor, ewaybillInfo).encode());
	}

	/**
	 * 实时上传车辆定位信息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_EXG_MSG_REAL_LOCATION
	 */
	public void sendExgMsgRealLocation(String vehicleNo, byte vehicleColor, byte[] gnssData) {
		log.debug("发送实时上传车辆定位信息");
		ChannelManager.sendMsgWithMainLinkPrefer(RealLocationEntity.create(vehicleNo, vehicleColor, gnssData).encode());
	}

	/********************** * 从链路车辆动态信息交换业务 ****************/

	/**
	 * 交换车辆定位信息消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_EXG_MSG_CAR_LOCATION
	 * 描述:上级平台通过该消息不间断地向车辆驶入区域所属的下级平台发送车辆定位信息,直到该车驶离该区域
	 */
	public void receiveExgMsgCarLocation(ByteBuf byteBuf, Channel channel) {
		log.info("接收到交换车辆定位信息消息");
		CarLocationEntity entity = new CarLocationEntity().decode(byteBuf);
		// TODO 保存定位信息

	}

	/**
	 * 车辆定位信息交换补发消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_EXG_MSG_HISTORY_ARCOSSAREA
	 * 描述:本业务在
	 * DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK应答成功后，立即开始交换。如果申请失败，则不进行数据转发，本条消息下级平台无需应答
	 */
	public void receiveExgMsgHistoryArcossarea(ByteBuf byteBuf, Channel channel) {
		log.info("接收车辆定位信息交换补发消息");

		HistoryArcossareaEntity entity = new HistoryArcossareaEntity().decode(byteBuf);

		// TODO 保存定位信息
		List<byte[]> data = entity.getGnssData();

	}

	/**
	 * 交换车辆静态信息消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_EXG_MSG_CAR_INFO
	 * 描述:在首次启动跨域车辆定位信息交换，或者以后交换过程中车辆静态信息有更新时，由上级平台向下级平台下发一次车辆静态信息。下级平台接收后自行更新该车辆静态信息，本条消息客户端无需应答
	 */
	public void receiveExgMsgCarInfo(ByteBuf byteBuf, Channel channel) {
		log.info("车辆定位信息交换补发消息");
		CarInfoEntity entity = new CarInfoEntity().decode(byteBuf);

		// TODO 保存或更新数据库车辆信息

	}

	/**
	 * 启动车辆定位信息交换请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_EXG_MSG_RETURN_STARTUP
	 * 描述:在有车辆进入非归属地区地理区域、人工指定车辆定位信息交换和应急状态监控车辆时，上级平台向下级平台发出启动车辆定位信息交换清求消息。下级平台收到此命令后
	 * 需要回复启动车辆定位信息交换应答消息给上级平台，即UP_EXG_MSG_RETURN_STARTUP_ACK
	 */
	public void receiveExgMsgReturnStartup(ByteBuf byteBuf, Channel channel) {
		ReturnStartupEntity entity = new ReturnStartupEntity().decode(byteBuf);

		sendExgMsgReturnStartupAck(entity.getBaseMsg().getVehicleNo(), entity.getBaseMsg().getVehicleColor());

	}

	/**
	 * 结束车辆定位信息交换请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_EXG_MSG_RETURN_END
	 * 描述:在进入非归属地区地理区域的车辆离开该地理区域、人工取消指定车辆定位信息交换和应急状态结束时，上级平台向下级平台发出结束车辆定位信息交换请求消息。下级平
	 * * 台收到该命令后应回复结束车辆定位信息交换应答消息，即 UP_EXG_MSG_RETURN_END_ACK
	 */
	public void receiveExgMsgReturnEnd(ByteBuf byteBuf, Channel channel) {
		ReturnEndEntity entity = new ReturnEndEntity().decode(byteBuf);

		sendExgMsgReturnEndAck(entity.getBaseMsg().getVehicleNo(), entity.getBaseMsg().getVehicleColor());

	}

	/**
	 * 申请交换指定车辆定位信息应答消息 链路类型:从链路 消息方向:上级平台台往下级平台
	 * 子业务类型标识:DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK
	 * 描述:应答下级平台申请交换指定车辆定位信息,请求消息.即 UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP
	 */
	public void receiveExgMsgApplyForMonitorStartupAck(ByteBuf byteBuf, Channel channel) {
		ApplyForMonitorStartupAckEntity entity = new ApplyForMonitorStartupAckEntity().decode(byteBuf);
	}

	/**
	 * 取消申请交换指定车辆定位信息应答消息 链路类型:从链路 消息方向:上级平台台往下级平台
	 * 子业务类型标识:DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK 描述:应答下级平台取消申清交换指定车辆定位信息清求消息
	 */
	public void receiveExgMsgApplyForMonitorEndAck(ByteBuf byteBuf, Channel channel) {
		ApplyForMonitorEndAckEntity entity = new ApplyForMonitorEndAckEntity().decode(byteBuf);
	}

	/**
	 * 补发车辆定位信息应答消息 链路类型:从链路 消息方向:上级平台台往下级平台
	 * 子业务类型标识:DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK
	 * 描述:本条消息是上级平台应答下级平台发送的补发车辆定位信息请求消息,即UP_EXG_MSG_APPLY_HISGNSSDATA_REQ
	 */
	public void receiveExgMsgApplyHisgnssDataAck(ByteBuf byteBuf, Channel channel) {
		ApplyHisgnssDataAckEntity entity = new ApplyHisgnssDataAckEntity().decode(byteBuf);
	}

	/**
	 * 上报驾驶员身份识别信息请求消息 链路类型:从链路 消息方向:上级平台台往下级平台
	 * 子业务类型标识:DOWN_EXG_MSG_REPORT_DRIVER_INFO 描述:上级平台向下级平台下发上报车辆驾驶员身份识别信息
	 */
	public void receiveExgMsgReportDriverInfo(ByteBuf byteBuf, Channel channel) {
		ReportDriverInfoEntity entity = new ReportDriverInfoEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See {RabbitMQConsumer.handleTakeTravelMessage}

	}

	/**
	 * 上报车辆电子运单请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_EXG_MSG_TAKE_EWAYBILL_REQ
	 * 描述:上级平台向下级平台下发上报车辆当前电子运单请求消息
	 */
	public void receiveExgMsgTakeEwaybillReq(ByteBuf byteBuf, Channel channel) {

		TakeEwaybillReqEntity entity = new TakeEwaybillReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See {RabbitMQConsumer.handleTakeTravelMessage}

	}
}

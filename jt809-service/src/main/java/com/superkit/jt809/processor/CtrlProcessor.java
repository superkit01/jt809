
package com.superkit.jt809.processor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.superkit.jt809.entity.ctrlMsg.EmergencyMonitoringAckEntity;
import com.superkit.jt809.entity.ctrlMsg.EmergencyMonitoringReqEntity;
import com.superkit.jt809.entity.ctrlMsg.MonitorVehicleAckEntity;
import com.superkit.jt809.entity.ctrlMsg.MonitorVehicleReqEntity;
import com.superkit.jt809.entity.ctrlMsg.TakePhotoAckEntity;
import com.superkit.jt809.entity.ctrlMsg.TakePhotoReqEntity;
import com.superkit.jt809.entity.ctrlMsg.TakeTravelAckEntity;
import com.superkit.jt809.entity.ctrlMsg.TakeTravelReqEntity;
import com.superkit.jt809.entity.ctrlMsg.TextInfoAckEntity;
import com.superkit.jt809.entity.ctrlMsg.TextInfoReqEntity;
import com.superkit.jt809.manager.ChannelManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 车辆监管业务
 */
@Slf4j
public class CtrlProcessor {
	
	private RabbitTemplate rabbitTemplate;
	
	public CtrlProcessor(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate=rabbitTemplate;
	}

	/************* 主链路车辆监管业务 *********************/
	/**
	 * 车辆单向监听应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_CTRL_MSG_MONITOR_VEHTCLE_ACK
	 * 描述:下级平台向上级平台上传车辆单向监听请求消息的应答
	 */
	public void sendMonitorVehicleAck(String vehicleNo, byte vehicleColor, byte result) {
		log.info("发送车辆单向监听应答消息");
		ChannelManager
				.sendMsgWithMainLinkPrefer(MonitorVehicleAckEntity.create(vehicleNo, vehicleColor, result).encode());
	}

	/**
	 * 车辆拍照应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_CTRL_MSG_TAKE_PHOTO_ACK
	 * 描述:下级平台应答上级平台发送的车辆拍照请求消息，上传图片信息到上级平台
	 */
	public void sendTakePhotoAck(String vehicleNo, byte vehicleColor, byte photoRspFlag, byte[] gnssData, byte lensId,
			long photoLen, byte size, byte type, byte[] photoContent) {
		log.info("发送车辆拍照应答消息");
		ChannelManager.sendMsgWithMainLinkPrefer(TakePhotoAckEntity
				.create(vehicleNo, vehicleColor, photoRspFlag, gnssData, lensId, photoLen, size, type, photoContent)
				.encode());
	}

	/**
	 * 下发车辆报文应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_CTRL_MSG_TEXT_INFO_ACK
	 * 描述:下级平台应答上级平台下发的报文是否成功到达指定车辆
	 */
	public void sendTextInfoAck(String vehicleNo, byte vehicleColor, long msgSequence, byte result) {
		log.info("下发车辆报文应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(
				TextInfoAckEntity.create(vehicleNo, vehicleColor, msgSequence, result).encode());
	}

	/**
	 * 上报车辆行驶记录应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_CTRL_MSG_TAKE_TRAVEL_ACK
	 * 描述:下级平台应答上级平台下发的上报车辆行驶记录请求消息，将车辆行驶记录数据上传至上级平台
	 */
	public void sendTakeTravelAck(String vehicleNo, byte vehicleColor, String travelDataInfo) {
		log.info("发送上报车辆行驶记录应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(
				TakeTravelAckEntity.create(vehicleNo, vehicleColor, travelDataInfo).encode());
	}

	/**
	 * 车辆应急接入监管平台应答消息 链路类型:主链路 消息方向:下级平台往上级平台
	 * 子业务类型标识:UP_CTRL_MSG_EMERGENCY_MONITORING_ACK 描述:下级平台应答上级平台下发的车辆应急接入监管平台请求消息应答
	 */
	public void sendEmergencyMonitoringAck(String vehicleNo, byte vehicleColor, byte result) {
		log.info("发送车辆应急接入监管平台应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(
				EmergencyMonitoringAckEntity.create(vehicleNo, vehicleColor, result).encode());
	}

	/***************** 从链路车辆监管业务 *****************/

	/**
	 * 车辆单向监听请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ
	 * 描述:上级平台向下级平台下发车辆单向监听清求消息
	 */
	public void receiveMonitorVehicleReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收车辆单向监听请求消息");
		MonitorVehicleReqEntity entity = new MonitorVehicleReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See RabbitMQConsumer.handleMonitorVehicleMessage

	}

	/**
	 * 车辆拍照请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_CTRL_MSG_TAKE_PHOTO_REQ
	 * 描述:上级平台向下级平台下发对某指定车辆的拍照请求消息
	 */
	public void receiveTakePhotoReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收到车辆拍照请求消息");
		TakePhotoReqEntity entity = new TakePhotoReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See {RabbitMQConsumer.handleTakePhotoMessage}

	}

	/**
	 * 下发车辆报文请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_CTRL_MSG_TEXT_INFO_REQ
	 * 描述:用于上级平台向下级平台下发报文到某指定车辆
	 */
	public void receiveTextInfoReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收到下发车辆报文请求消息");
		TextInfoReqEntity entity = new TextInfoReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See {RabbitMQConsumer.handleTextInfoMessage}
	}

	/**
	 * 上报车辆行驶记录请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_CTRL_MSG_TAKE_TRAVEL_REQ
	 * 描述:上级平台向下级平台下发上报车辆行驶记录请求消息
	 */
	public void receiveTakeTravelReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收到上报车辆行驶记录请求消息");

		TakeTravelReqEntity entity = new TakeTravelReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See {RabbitMQConsumer.F}

	}

	/**
	 * 车辆应急接入监管平台请求消息 链路类型:从链路 消息方向:上级平台台往下级平台
	 * 子业务类型标识:DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ
	 * 描述:发生应急情况时，政府监管平台需要及时监控该车辆时，就向该车辆归属的下级平台发送该命令
	 */
	public void receiveEmergencyMonitoringReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收到车辆应急接入监管平台请求消息");
		EmergencyMonitoringReqEntity entity = new EmergencyMonitoringReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808将请求发给给808平台,在MQ中发布下发结果，这边消费结果后再发给上级平台
		// @See {RabbitMQConsumer.handleEmergencyMonitoringMessage}
	}
}

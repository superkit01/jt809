package com.superkit.jt809.processor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.superkit.jt809.dto.BaseMsgCarInfo;
import com.superkit.jt809.entity.baseMsg.VehicleAddedAckEntity;
import com.superkit.jt809.entity.baseMsg.VehicleAddedEntity;
import com.superkit.jt809.manager.ChannelManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 车辆静态信息交换业务
 */
@Slf4j
public class BaseMsgProcessor {

	private RabbitTemplate rabbitTemplate;
	
	public BaseMsgProcessor(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate=rabbitTemplate;
	}

	
	/************ 主链路车辆静态信息交换业务 *****************/

	/**
	 * 补报车辆静态信息应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_BASE_MSG_VEHICLE_ADDED_ACK
	 * 描述:上级平台应答下级平台发送的补报车辆静态信息清求消息
	 */
	public void sendVehicleAddedAck(String vehicleNo, byte vehicleColor, String carInfo) {
		log.info("发送补报车辆静态信息应答消息");
		ChannelManager
				.sendMsgWithMainLinkPrefer(VehicleAddedAckEntity.create(vehicleNo, vehicleColor, carInfo).encode());
	}

	/************ 从链路车辆静态信息交换业务 *****************/

	/**
	 * 补报车辆静态信息请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_BASE_MSG_VEHICLE_ADDED
	 * 描述:上级平台在接收到车辆定位信息后，发现该车辆静态信息在上级平台不存在，上级平台向下级平台下发补报该车辆静态信息的请求消息
	 */
	public void receiveVehicleAdded(ByteBuf byteBuf, Channel channel) {
		log.info("接收补报车辆静态信息请求消息");
		VehicleAddedEntity entity = new VehicleAddedEntity().decode(byteBuf);

		String vehicleNo = entity.getBaseMsg().getVehicleNo().trim();
		byte vehicleColor = entity.getBaseMsg().getVehicleColor();

		// TODO 根据车牌号从808平台获取数据

		// 组装消息体
		String carInfo = new BaseMsgCarInfo().setVIN("").setVEHICLE_COLOR("").setVEHICLE_TYPE("").setTRANS_TYPE("")
				.setVEHICLE_NATIONALITY("").setOWERS_ID("").setOWERS_NAME("").setOWERS_ORIG_ID("").setOWERS_TEL("")
				.toString();

		sendVehicleAddedAck(vehicleNo, vehicleColor, carInfo);

	}

}

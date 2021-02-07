package com.superkit.jt809.processor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.superkit.jt809.entity.warnMsg.AdptInfoEntity;
import com.superkit.jt809.entity.warnMsg.ExgInformEntity;
import com.superkit.jt809.entity.warnMsg.InformTipsEntity;
import com.superkit.jt809.entity.warnMsg.UrgeTodoAckEntity;
import com.superkit.jt809.entity.warnMsg.UrgeTodoReqEntity;
import com.superkit.jt809.manager.ChannelManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 车辆报警信息业务
 */
@Slf4j
public class WarnProcessor {

	private RabbitTemplate rabbitTemplate;

	public WarnProcessor(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	/*********************** 主链路车辆报警信息业务 ***************/

	/**
	 * 报警督办应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_WARN_MSG_URGE_TODO_ACK
	 * 描述:下级平台应答上级平台下发的报警督办请求消息，向上级平台上报车辆的报瞥处理结果
	 */
	public void sendUrgeTodoAck(String vehicleNo, byte vehicleColor, long supervisionId, byte result) {
		log.info("发送报警督办应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(
				UrgeTodoAckEntity.create(vehicleNo, vehicleColor, supervisionId, result).encode());
	}

	/**
	 * 上报报警信息消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_WARN_MSG_ADPT_INFO
	 * 描述:下级平台向上级平台上报某车辆的报警信息,本条消息上级平台无需应答
	 */
	public void sendAdptInfo(String vehicleNo, byte vehicleColor, byte warnSrc, int warnType, long warnTime,
			long infoId, int infoLength, String infoContent) {
		log.debug("发送上报报警信息消息");

		ChannelManager.sendMsgWithMainLinkPrefer(AdptInfoEntity
				.create(vehicleNo, vehicleColor, warnSrc, warnType, warnTime, infoId, infoContent).encode());
	}

	/******************** 从链路车辆报警信息业务 ******************/

	/**
	 * 报警督办请求消息 链路类型:从链路 消息方向:上级平台往下级平台 子业务类型标识:DOWN_WARN_MSG_URGE_TODO_REQ
	 * 描述:上级平台向车辆归属下级平台下发本消息，催促其及时处理相关车辆的报警信息
	 */
	public void receiveUrgeTodoReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收报警督办请求消息");
		UrgeTodoReqEntity entity = new UrgeTodoReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See RabbitMQConsumer.handleUrgeTodoMessage

	}

	/**
	 * 报警预警消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_WARN_MSG_INFORM_TIPS
	 * 描述:用于上级平台向车辆归属或车辆跨域下级平台下发相关车辆的报警预警或运行提示信息,本条消息下级平台无需应答
	 */
	public void receiveInformTips(ByteBuf byteBuf, Channel channel) {
		log.info("接收报警预警消息");
		InformTipsEntity entity = new InformTipsEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// TODO 保存消息

	}

	/**
	 * 实时交换报警信息消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_WARN_MSG_EXG_INFORM
	 * 描述:用于上级平台向车辆跨域目的地下级平台下发相关车辆的当前报警情况,本条消息下级平台无需应答
	 */
	public void receiveExgInform(ByteBuf byteBuf, Channel channel) {
		log.info("接收实时交换报警信息消息");
		ExgInformEntity entity = new ExgInformEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// TODO 保存消息

	}
}

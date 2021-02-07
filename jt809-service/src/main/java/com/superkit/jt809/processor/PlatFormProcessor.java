package com.superkit.jt809.processor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.superkit.jt809.entity.platformMsg.InfoAckEntity;
import com.superkit.jt809.entity.platformMsg.InfoReqEntity;
import com.superkit.jt809.entity.platformMsg.PostQueryAckEntity;
import com.superkit.jt809.entity.platformMsg.PostQueryReqEntity;
import com.superkit.jt809.manager.ChannelManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 平台间信息交互业务
 */
@Slf4j
public class PlatFormProcessor {

	private RabbitTemplate rabbitTemplate;

	public PlatFormProcessor(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	/********************* 主链路平台间信息交互业务 ***************/
	/**
	 * 平台查岗应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_PLATFORM_MSG_POST_QUERY_ACK
	 * 描述:下级平台应答上级平台发送的不定期平台查岗消息
	 */
	public void sendPostQueryAck(long infoId, String infoContent) {
		log.info("发送平台查岗应答消息");

		ChannelManager.sendMsgWithMainLinkPrefer(PostQueryAckEntity.create(infoId, infoContent).encode());
	}

	/**
	 * 下发平台间报文应答消息 链路类型:主链路 消息方向:下级平台往上级平台 子业务类型标识:UP_PLATFORM_MSG_INFO_ACK
	 * 描述:下级平台收到上级平台发送的下发平台间报文请求消息后，发送应答消息
	 */
	public void sendInfoAck(long infoId) {
		log.info("发送下发平台间报文应答消息");
		ChannelManager.sendMsgWithMainLinkPrefer(InfoAckEntity.create(infoId).encode());
	}

	/********************* 从链路平台间信息交互业务 *****************/

	/**
	 * 平台查岗请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_PLATFORM_MSG_POST_QUERY_REQ
	 * 描述:上级平台不定期向下级平台发送平台查岗信息
	 */
	public void receivePostQueryReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收平台查岗请求消息");
		PostQueryReqEntity entity = new PostQueryReqEntity().decode(byteBuf);

		rabbitTemplate.convertAndSend(entity);
		// 后续：808消费该消息 ,再将处理结果发布到MQ，这边消费结果后再发给上级平台
		// @See RabbitMQConsumer.handlePostQueryMessage

	}

	/**
	 * 下发平台间报文请求消息 链路类型:从链路 消息方向:上级平台台往下级平台 子业务类型标识:DOWN_PLATFORM_MSG_INFO_REQ
	 * 描述:上级平台不定期向下级平台下发平台间报文
	 */
	public void receiveInfoReq(ByteBuf byteBuf, Channel channel) {
		log.info("接收下发平台间报文请求消息");
		InfoReqEntity entity = new InfoReqEntity().decode(byteBuf);
		// TODO 保存

		sendInfoAck(entity.getInfoId());

	}
}

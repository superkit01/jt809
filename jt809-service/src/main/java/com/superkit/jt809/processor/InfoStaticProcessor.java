package com.superkit.jt809.processor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.superkit.jt809.entity.infoStatic.TotalRecvBackMsgEntity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 信息统计业务
 */
@Slf4j
public class InfoStaticProcessor {

	private RabbitTemplate rabbitTemplate;

	public InfoStaticProcessor(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	/**
	 * 接收车辆定位信息数量通知消息 链路类型：从链路 消息方向:上级平台往下级平台 业务类型标识: DOWN_TOTAL_RECV_BACK_MSG
	 * 描述：上级平台向下级平台定星通知已经收到下级平台上传的车辆定位信息数量(如:每收到10,000 条车辆定位信息通知一次)
	 */
	public void receiveTotalRecvBackMsg(ByteBuf byteBuf, Channel channel) {

		TotalRecvBackMsgEntity entity = new TotalRecvBackMsgEntity().decode(byteBuf);
		long infoTotal = entity.getDynamicInfoTotal();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getStartTime()), ZoneOffset.UTC);
		LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getEndTime()), ZoneOffset.UTC);

		log.info("接收车辆定位信息数量,起始时间:{}，结束时间:{}，共收到{}条消息", startTime.format(formatter), endTime.format(formatter),
				infoTotal);

	}
}

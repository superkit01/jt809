package com.superkit.jt809.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.handler.RabbitmqDto;
import com.superkit.jt809.handler.RabbitmqHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableRabbit
@Slf4j
public class RabbitmqConsumer {
	@Autowired
	RabbitmqHandler handler;

	@RabbitListener(bindings = {
			@QueueBinding(value = @Queue(value = "JT809_SUBSCRIBLE_MSG", durable = "true"), exchange = @Exchange(type = ExchangeTypes.DIRECT, value = "JT809_SUBSCRIBLE_MSG", durable = "true"), key = "JT809_SUBSCRIBLE_MSG") })
	public void handleMessage(String message) throws Exception {
		log.debug("MQ消费,message：{}", message);

		RabbitmqDto<?> entity = new ObjectMapper().readValue(message, RabbitmqDto.class);

		switch (entity.getMsgId()) {
		/**
		 * 车辆动态信息交换
		 */
		case MsgConstant.PrimaryServiceType.DOWN_EXG_MSG:
			switch (entity.getDataType()) {
			case MsgConstant.SubServiceType.DOWN_EXG_MSG_REPORT_DRIVER_INFO:
				handler.handleReportDriverInfoMessage(entity);
				break;
			case MsgConstant.SubServiceType.DOWN_EXG_MSG_TAKE_EWAYBILL_REQ:
				handler.handleTakeEwaybillMessage(entity);
				break;
			}
			break;

		/**
		 * 平台间信息交互类
		 * 
		 */
		case MsgConstant.PrimaryServiceType.DOWN_PLATFORM_MSG:
			switch (entity.getDataType()) {
			case MsgConstant.SubServiceType.DOWN_PLATFORM_MSG_POST_QUERY_REQ:// 平台查岗应答
				handler.handlePostQueryMessage(entity);
				break;
			}
			break;

		/**
		 * 车辆报警信息交互类
		 * 
		 */
		case MsgConstant.PrimaryServiceType.DOWN_WARN_MSG:
			switch (entity.getDataType()) {
			case MsgConstant.SubServiceType.DOWN_WARN_MSG_URGE_TODO_REQ:// 报警督办应答
				handler.handleUrgeTodoMessage(entity);
				break;
			}

			break;
		/**
		 * 车辆监管类
		 */
		case MsgConstant.PrimaryServiceType.DOWN_CTRL_MSG:
			switch (entity.getDataType()) {
			case MsgConstant.SubServiceType.DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ:// 单向监听响应
				handler.handleMonitorVehicleMessage(entity);
				break;
			case MsgConstant.SubServiceType.DOWN_CTRL_MSG_TAKE_PHOTO_REQ:// 拍照响应
				handler.handleTakePhotoMessage(entity);
				break;
			case MsgConstant.SubServiceType.DOWN_CTRL_MSG_TEXT_INFO_REQ:// 下发车辆报文响应
				handler.handleTextInfoMessage(entity);
				break;
			case MsgConstant.SubServiceType.DOWN_CTRL_MSG_TAKE_TRAVEL_REQ:// 上报车辆行驶记录响应
				handler.handleTakeTravelMessage(entity);
				break;
			case MsgConstant.SubServiceType.DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ:// 上报车辆应急接入监管平台应答
				handler.handleEmergencyMonitoringMessage(entity);
				break;
			}
			break;
		default:
			log.error("unknown msgId:{}", entity.getMsgId());
			break;
		}
	}
}

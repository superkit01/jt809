package com.superkit.jt809.processor;

import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.superkit.jt809.JT809Config;
import com.superkit.jt809.entity.linkManager.MainConnectReqEntity;
import com.superkit.jt809.entity.linkManager.MainConnectRspEntity;
import com.superkit.jt809.entity.linkManager.MainDisConnectInformEntity;
import com.superkit.jt809.entity.linkManager.MainDisConnectReqEntity;
import com.superkit.jt809.entity.linkManager.MainDisConnectRspEntity;
import com.superkit.jt809.entity.linkManager.MainLinkTestReqEntity;
import com.superkit.jt809.entity.linkManager.MainLinkTestRspEntity;
import com.superkit.jt809.entity.linkManager.SubCloseLinkInformEntity;
import com.superkit.jt809.entity.linkManager.SubConnectReqEntity;
import com.superkit.jt809.entity.linkManager.SubConnectRspEntity;
import com.superkit.jt809.entity.linkManager.SubDisConnectInformEntity;
import com.superkit.jt809.entity.linkManager.SubDisConnectReqEntity;
import com.superkit.jt809.entity.linkManager.SubDisConnectRspEntity;
import com.superkit.jt809.entity.linkManager.SubLinkTestReqEntity;
import com.superkit.jt809.entity.linkManager.SubLinkTestRspEntity;
import com.superkit.jt809.enums.LinkManagerEnum;
import com.superkit.jt809.enums.LinkTypeEnum;
import com.superkit.jt809.manager.ChannelManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 链路管理业务
 */
@Slf4j
public class LinkManagerProcessor {
	private RabbitTemplate rabbitTemplate;
	
	public LinkManagerProcessor(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate=rabbitTemplate;
	}
	
	/**
	 * 主链路登录请求消息 链路类型:主链路 消息方向:下级平台往上级平台 业务数据类型标识: UP_CONNECT_REQ
	 * 描述：下级平台向上级平台发送用户名和密码等登录信息
	 */
	public void sendMainConnectReq(Channel channel) {
		long userId = JT809Config.userId;
		String password = JT809Config.password;
		String serverIp = JT809Config.localIp;
		Integer serverPort = JT809Config.localPort;
		log.info("【发送】主链路登录请求 userId:{},password:{},clientIp:{},clientPort:{}", userId, password, serverIp, serverPort);

		ChannelManager.sendMsg(channel, MainConnectReqEntity.create(userId, password, serverIp, serverPort).encode());
	}

	/**
	 * 主链路登录应答消息 链路类型:主链路 消息方向:上级平台往下级平台 业务数据类型标识:UP_CONNECT_RSP
	 * 描述：上级平台对下级平台登录请求信息、进行安全验证后，返回相应的验证结果
	 */
	public void receiveMainConnectRsp(ByteBuf byteBuf, Channel channel) {
		MainConnectRspEntity entity = new MainConnectRspEntity().decode(byteBuf);
		if (LinkManagerEnum.MainConnectRspEnum.SUCCESS.getValue() == entity.getResult()) {
			log.info("【接收】主链路登录成功");
			ChannelManager.addChannel(LinkTypeEnum.MAIN_LINKS, channel);

			ChannelManager.VERIFY_CODE = entity.getVerifyCode();
			log.info("主链路登录verifyCode：{}", entity.getVerifyCode());

		} else {
			log.error("【接收】主链路登录失败:{}", entity.getResult());
		}

	}

	/**
	 * 主链路注销请求消息 链路类型:主链路 消息方向：下级平台往上级平台 业务数据类型标识：UP_DISCONNECT_REQ
	 * 描述：下级平台在中断与上级平台的主链路连接时，应向上级平台发送主链路注销请求消息
	 */
	public void sendMainDisConnectReq() {
		Long userId = JT809Config.userId;
		String password = JT809Config.password;
		log.info("【发送】主链路注销请求 userId:{},password:{}", userId, password);
		ChannelManager.sendMsgWithMainLinkPrefer(MainDisConnectReqEntity.create(userId, password).encode());
	}

	/**
	 * 主链路注销应答消息 链路类型:主链路 消息方向:上级平台往下级平台 业务数据类型标识:UP_DISCONNECT_RSP
	 * 描述：上级平台收到下级平台发送的主链路注销请求消息后，向下级平台返回主链路注销应答消息，并记录链路注销日志，下级平台接收到应答消息后，可中断主从链路联接
	 */
	public void receiveMainDisConnectRsp(ByteBuf byteBuf, Channel channel) {

		MainDisConnectRspEntity entity = new MainDisConnectRspEntity().decode(byteBuf);

		log.info("【接收】主链路注销应答");
		// 中断主从链接
		ChannelManager.closeAllChannel();
	}

	/**
	 * 主链路连接保持请求消息 链路类型:主链路 消息方向:下级平台往上级平台 业务数据类型标识:UP_LINKTEST_REQ
	 * 描述：下级平台向上级平台发送主链路连接保持清求消息，以保持主链路的连接
	 */
	public void sendMainLinkTestReq() {
		log.info("【发送】主链路心跳");

		// 主链路连续三分钟未接收到上级平台响应，主动断开数据传输从链路
		if (ChannelManager.mainLinkTimeOutTimes.get() > ChannelManager.HEART_BEAT_LIMIT) {
			log.error("主链路心跳超时");
			sendMainDisConnectInform(LinkManagerEnum.MainDisConnectInformEnum.MAIN_LINK_CLOSED.getValue());
			ChannelManager.closeSubLink();
		}
		ChannelManager.mainLinkTimeOutTimes.incrementAndGet();
		// 发送心跳包
		ChannelManager.sendMsg(ChannelManager.getChannel(LinkTypeEnum.MAIN_LINKS),
				MainLinkTestReqEntity.create().encode());
	}

	/**
	 * 主链路连接保持请求消息(主链路心跳) 链路类型:主链路 消息方向:上级平台往下级平台 业务数据类型标识:UP_LINKTEST_RSP
	 * 描述：上级平台收到下级平台的主链路连接保持请求消息后，向下级平台返回.主链路连接保持应答消息，保持主链路的连接状态
	 */
	public void receiveMainLinkTestRsp(ByteBuf byteBuf, Channel channel) {
		log.info("【接收】主链路心跳应答");
		MainLinkTestRspEntity entity = new MainLinkTestRspEntity().decode(byteBuf);

		// 接受到心跳包，心跳超时计次归零
		ChannelManager.mainLinkTimeOutTimes.set(0);

	}

	/**
	 * 从链路断开通知消息 链路类型:主链路 消息方向:上级平台往下级平台 业务数据类型标识:DOWN_DISCONNECT_INFORM 描述： 情景
	 * 1:上级平台与下级平台的从链路中断后，重连二次仍未成功时，上级平台通过主链路发送本消息给下级平台。 情景
	 * 2:上级平台作为客户端向下级平台登录时，根据之前收到的 IP 地址及端口无法连接到下级平台服务端时发送本消息通知下级平台
	 */
	public void receiveSubDisConnectInform(ByteBuf byteBuf, Channel channel) {
		SubDisConnectInformEntity entity = new SubDisConnectInformEntity().decode(byteBuf);
		log.info("【接收】从链路断开通知:{}", entity.getReasonCode());

		// 关闭从链接
		ChannelManager.closeSubLink();
	}

	/******************************************************************************************************************************************************************/

	/**
	 * 主链路断开通知消息 链路类型:从链路 消息方向:下级平台往上级平台 业务数据类型标识:UP_DISCONNECT_INFORM
	 * 描述：当主链路中断后，下级平台可通过从链路向上级平台发送本消息通知上级平台主链路中断，本条消息无需被通知方应答
	 */
	public void sendMainDisConnectInform(byte errorCode) {
		log.info("【发送】主链路断开通知 errorCode:{}", errorCode);

		ChannelManager.sendMsg(ChannelManager.getChannel(LinkTypeEnum.SUB_LINKS),
				MainDisConnectInformEntity.create(errorCode).encode());
		// 关闭主链接
		ChannelManager.closeMainLink();
	}

	/**
	 * 下级平台主动关闭主从链路通知消息 链路类型:从链路 消息方向:下级平台往上级平台 业务数据类型标识:UP_CLOSELINK_INFORM
	 * 描述：下级平台作为服务端，发现从链路出现异常时，下级平台通过从链路向上级平台发送本消息，通知上级平台下级平台即将关闭主从链路，本条消息无需被通知方应答;
	 */
	public void sendSubCloseLinkInform(byte reasonCode) {
		log.info("【发送】下级平台主动关闭主从链路通知 reasonCode:{}", reasonCode);

		ChannelManager.sendMsg(ChannelManager.getPreferSubLink(), SubCloseLinkInformEntity.create(reasonCode).encode());
		ChannelManager.closeAllChannel();
	}

	/**
	 * 从链路连接请求消息 链路类型:从链路 消息方向:上级平台往下级平台 业务数据类型标识:DOWN_CONNECT_REQ
	 * 描述：主链路建立连接后，上级平台向下级平台发送从链路连接清求消息，以建立从链路连接
	 */
	public void receiveSubConnectReq(ByteBuf byteBuf, Channel channel) {
		log.info("【接收】上级平台从链路登录请求");
		SubConnectReqEntity entity = new SubConnectReqEntity().decode(byteBuf);

		ChannelManager.addChannel(LinkTypeEnum.SUB_LINKS, channel);
		if (Objects.equals(entity.getVerifyCode(), ChannelManager.VERIFY_CODE)) {
			sendSubConnectRsp(LinkManagerEnum.SubConnectRspEnum.SUCCESS.getValue());
		} else {
			sendSubConnectRsp(LinkManagerEnum.SubConnectRspEnum.WRONG_VERIFY_CODE.getValue());
		}
	}

	/**
	 * 从链路连接应答信息 链路类型:从链路 消息方问:下级平台往上级平台 业务数据类型标识:DOWN_CONNNECT_RSP
	 * 描述：下级平台作为服务器端向上级平台客户端返回从链路连接应答消息，上级平台在接收到该应答消息结果后，根据结果进行链路连接处理
	 */
	public void sendSubConnectRsp(byte result) {
		log.info("【发送】上级平台从链路登录应答 result:{}", result);
		ChannelManager.sendMsg(ChannelManager.getChannel(LinkTypeEnum.SUB_LINKS),
				SubConnectRspEntity.create(result).encode());
	}

	/**
	 * 从链路注销请求消息 链路类型:从链路 消息方向:.上级平台往下级平台 业务数据类型标识:DOWN_DISCONNECT_REQ
	 * 描述：从链路建立后，上级平台在取消该链路时，应向下级平台发送从链路注销请求消息
	 */
	public void receiveSubDisConnectReq(ByteBuf byteBuf, Channel channel) {
		SubDisConnectReqEntity entity = new SubDisConnectReqEntity().decode(byteBuf);

		// 发送从链路连接应答信息
		if (Objects.equals(String.valueOf(entity.getVerifyCode()), ChannelManager.VERIFY_CODE)) {
			sendSubDisConnectRsp();
			ChannelManager.closeSubLink();
		}
	}

	/**
	 * 从链路注销应答消息 链路类型:从链路 消息方向:下级平台往上级平台 业务数据类型构之识:DOWN_DISCONNECT_RSP
	 * 描述：下级平台在收到上级平台发送的从链路注销请求消息后，返回从链路注销应答消息，记录相关日志，中断该从链路
	 */
	public void sendSubDisConnectRsp() {
		ChannelManager.sendMsg(ChannelManager.getPreferSubLink(), new SubDisConnectRspEntity().encode());
	}

	/**
	 * 从链路连接保持请求消息 链路类型:从链路 消息方向:上级平台往下级平台 业务数据类型标识:DOWN_LINKTEST_REQ
	 * 描述：从链路建立成功后，上级平台向下级平台发送从链路连接保持请求消息，以保持从链路的连接状态
	 */
	public void receiveSubLinkTestReq(ByteBuf byteBuf, Channel channel) {
		SubLinkTestReqEntity entity = new SubLinkTestReqEntity().decode(byteBuf);
		// 接受到心跳包，心跳超时计次归零
		ChannelManager.subLinkTimeOutTimes.set(0);
		sendSubLinkTestRsp(channel);
	}

	/**
	 * 从链路连接保持应答消息 链路类型：从链路 消息方向:上级平台往下级平台 业务数据类型标识:DOWN_LINKTEST_RSP
	 * 描述：下级平台收到上级平台链路连接保持请求消息后，向上级平台返回从链路连接保持应答消息，保持从链路连接状态
	 */
	public void sendSubLinkTestRsp(Channel channel) {
		log.info("【发送】从链路心跳应答");
		ChannelManager.sendMsg(ChannelManager.getChannel(LinkTypeEnum.SUB_LINKS), new SubLinkTestRspEntity().encode());
	}
}

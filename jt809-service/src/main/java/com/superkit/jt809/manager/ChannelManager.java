package com.superkit.jt809.manager;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.superkit.jt809.enums.LinkTypeEnum;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * 主从链路管理器
 */
public class ChannelManager {

	// 连续几次检测到心跳超时自动断开
	public static final int HEART_BEAT_LIMIT = 3;
	
	public static volatile Long VERIFY_CODE;

	// 主链路超时次数
	public static volatile AtomicInteger mainLinkTimeOutTimes = new AtomicInteger(0);

	// 从链路超时次数
	public static volatile AtomicInteger subLinkTimeOutTimes = new AtomicInteger(0);

	private static volatile ConcurrentHashMap<LinkTypeEnum, Channel> channelMap = new ConcurrentHashMap<>(2);

	public static void addChannel(LinkTypeEnum linkType, Channel channel) {
		channelMap.put(linkType, channel);
	}

	public static Channel getChannel(LinkTypeEnum linkType) {
		return channelMap.get(linkType);
	}

	/**
	 * 关闭主从链路
	 */
	public static void closeAllChannel() {
		closeMainLink();
		closeSubLink();
	}

	/**
	 * 关闭主链路
	 */
	public static void closeMainLink() {
		if (channelMap.containsKey(LinkTypeEnum.MAIN_LINKS) && channelMap.get(LinkTypeEnum.MAIN_LINKS).isOpen()) {
			channelMap.get(LinkTypeEnum.MAIN_LINKS).close();
			channelMap.remove(LinkTypeEnum.MAIN_LINKS);
		}
	}

	/**
	 * 关闭从链路
	 */
	public static void closeSubLink() {
		if (channelMap.containsKey(LinkTypeEnum.SUB_LINKS) && channelMap.get(LinkTypeEnum.SUB_LINKS).isOpen()) {
			channelMap.get(LinkTypeEnum.SUB_LINKS).close();
			channelMap.remove(LinkTypeEnum.SUB_LINKS);
		}
	}

	/**
	 * 优先获取主链路，主链路断开返回从链路
	 */
	public static Channel getPreferMainLink() {
		if (channelMap.containsKey(LinkTypeEnum.MAIN_LINKS) && channelMap.get(LinkTypeEnum.MAIN_LINKS).isOpen()) {
			return channelMap.get(LinkTypeEnum.MAIN_LINKS);
		} else if (channelMap.containsKey(LinkTypeEnum.SUB_LINKS) && channelMap.get(LinkTypeEnum.SUB_LINKS).isOpen()) {
			return channelMap.get(LinkTypeEnum.SUB_LINKS);
		} else {
			return null;
		}
	}

	/**
	 * 优先获取从链路，从链路断开返回主链路
	 */
	public static Channel getPreferSubLink() {
		if (channelMap.containsKey(LinkTypeEnum.SUB_LINKS) && channelMap.get(LinkTypeEnum.SUB_LINKS).isOpen()) {
			return channelMap.get(LinkTypeEnum.SUB_LINKS);
		} else if (channelMap.containsKey(LinkTypeEnum.MAIN_LINKS)
				&& channelMap.get(LinkTypeEnum.MAIN_LINKS).isOpen()) {
			return channelMap.get(LinkTypeEnum.MAIN_LINKS);
		} else {
			return null;
		}
	}

	/**
	 * 通过主链路发送消息，主链路断开时通过从链路发送
	 */
	public static void sendMsgWithMainLinkPrefer(byte[] bytes) {
		Channel channel = ChannelManager.getPreferMainLink();
		Optional.ofNullable(channel).ifPresent(i -> channel.writeAndFlush(Unpooled.wrappedBuffer(bytes)));
	}

	/**
	 * 通过从链路发送消息，主链路断开时通过主链路发送
	 */
	public static void sendMsgWithSubLinkPrefer(byte[] bytes) {
		Channel channel = ChannelManager.getPreferSubLink();
		Optional.ofNullable(channel).ifPresent(i -> channel.writeAndFlush(Unpooled.wrappedBuffer(bytes)));
	}
	
	public static void sendMsg(Channel channel,byte[] bytes) {
		Optional.ofNullable(channel).ifPresent(i -> channel.writeAndFlush(Unpooled.wrappedBuffer(bytes)));
	}
}

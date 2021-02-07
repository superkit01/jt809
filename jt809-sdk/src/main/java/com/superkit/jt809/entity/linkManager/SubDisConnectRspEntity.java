package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * 从链路注销应答消息
 * <p>
 * 业务数据类型标识:DOWN_DISCONNECT_RSP
 * <p>
 * 下级平台在收到上级平台发送的从链路注销请求消息后，返回从链路注销应答消息，记录相关日志，中断该从链路
 */
@Setter
@Getter
public class SubDisConnectRspEntity implements Codec<SubDisConnectRspEntity> {

	private BaseEntity base;

	/**
	 * 数据体为空
	 */

	@Override
	public SubDisConnectRspEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		return this;
	}

	public static SubDisConnectRspEntity create(long userId, String password) {
		SubDisConnectRspEntity baseMsg = new SubDisConnectRspEntity();

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.DOWN_DISCONNECT_RSP, new byte[0]);
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}
}

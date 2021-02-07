package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * 从链路连接保持应答消息
 * <p>
 * 业务数据类型标识:DOWN_LINKTEST_RSP
 * <p>
 * 下级平台收到上级平台链路连接保持请求消息后，向上级平台返回从链路连接保持应答消息，保持从链路连接状态
 */
@Setter
@Getter
public class SubLinkTestRspEntity implements Codec<SubLinkTestRspEntity> {

	private BaseEntity base;

	/**
	 * 数据体为空
	 */

	@Override
	public SubLinkTestRspEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		return this;
	}

	public static SubLinkTestRspEntity create(long userId, String password) {
		SubLinkTestRspEntity baseMsg = new SubLinkTestRspEntity();

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.DOWN_LINKTEST_RSP, new byte[0]);
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}
}

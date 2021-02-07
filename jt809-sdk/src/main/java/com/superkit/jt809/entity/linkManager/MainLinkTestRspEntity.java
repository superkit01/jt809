package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * 主链路连接保持应答消息
 * <p>
 * 业务数据类型标识:UP_LINKTEST_RSP
 * <p>
 * 上级平台收到下级平台的主链路连接保持请求消息后，向下级平台返回.主链路连接保持应答消息，保持主链路的连接状态
 */
@Setter
@Getter
public class MainLinkTestRspEntity implements Codec<MainLinkTestRspEntity> {

	private BaseEntity base;

	/**
	 * 数据体为空
	 */

	@Override
	public MainLinkTestRspEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		return this;
	}

	public static MainLinkTestRspEntity create(long userId, String password) {
		MainLinkTestRspEntity baseMsg = new MainLinkTestRspEntity();

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_LINKTEST_RSP, new byte[0]);
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

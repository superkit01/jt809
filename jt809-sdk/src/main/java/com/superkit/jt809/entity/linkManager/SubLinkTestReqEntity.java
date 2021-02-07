package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * 从链路连接保持请求消息
 * <p>
 * 业务数据类型标识:DOWN_LINKTEST_REQ
 * <p>
 * 从链路建立成功后，上级平台向下级平台发送从链路连接保持请求消息，以保持从链路的连接状态
 */
@Setter
@Getter
public class SubLinkTestReqEntity implements Codec<SubLinkTestReqEntity> {

	private BaseEntity base;

	/**
	 * 数据体为空
	 */

	@Override
	public SubLinkTestReqEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		return this;
	}

	public static SubLinkTestReqEntity create(long userId, String password) {
		SubLinkTestReqEntity baseMsg = new SubLinkTestReqEntity();

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.DOWN_LINKTEST_REQ, new byte[0]);
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}
}

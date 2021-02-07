package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 从链路连接请求消息
 * <p>
 * 业务数据类型标识: DOWN_CONNECT_REQ
 * <p>
 * 主链路建立连接后，上级平台向下级平台发送从链路连接清求消息，以建立从链路连接
 */
@Setter
@Getter
@NoArgsConstructor
public class SubConnectReqEntity implements Codec<SubConnectReqEntity> {

	private BaseEntity base;

	private long verifyCode; // 校验码 byte[4]

	@Override
	public SubConnectReqEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.verifyCode = BufUtil.buf2UnsignedLong(msgbody);

		return this;
	}

	public static SubConnectReqEntity create(long verifyCode) {
		SubConnectReqEntity baseMsg = new SubConnectReqEntity();

		baseMsg.verifyCode = verifyCode;

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.DOWN_CONNECT_REQ,
				ByteUtil.int2Byte((int) baseMsg.verifyCode));
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

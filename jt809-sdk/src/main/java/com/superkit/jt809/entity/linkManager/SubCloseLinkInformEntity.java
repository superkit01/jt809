package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 下级平台主动关闭主从链路通知消息
 * <p>
 * 业务数据类型标识:UP_CLOSELINK_INFORM
 * <p>
 * 下级平台作为服务端，发现从链路出现异常时，下级平台通过从链路向上级平台发送本消息，通知上级平台下级平台即将关闭主从链路，本条消息无需被通知方应答;
 */
@Setter
@Getter
@NoArgsConstructor
public class SubCloseLinkInformEntity implements Codec<SubCloseLinkInformEntity> {

	private BaseEntity base;
	/**
	 * 错误代码：定义如下 0x00:网关重启； 0x01：其他原因
	 */
	private byte reasonCode;

	@Override
	public SubCloseLinkInformEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.reasonCode = msgbody.getByte(0);

		return this;
	}

	public static SubCloseLinkInformEntity create(byte reasonCode) {
		SubCloseLinkInformEntity baseMsg = new SubCloseLinkInformEntity();

		baseMsg.reasonCode = reasonCode;

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_CLOSELINK_INFORM,
				new byte[] { baseMsg.reasonCode });
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

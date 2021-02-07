package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 从链路断开通知消息
 * <p>
 * 业务数据类型标识:DOWN_DISCONNECT_INFORM
 * <p>
 * 情景 1:上级平台与下级平台的从链路中断后，重连二次仍未成功时，上级平台通过主链路发送本消息给下级平台。 情景
 * 2:上级平台作为客户端向下级平台登录时，根据之前收到的 IP 地址及端口无法连接到下级平台服务端时发送本消息通知下级平台
 */
@Setter
@Getter
@NoArgsConstructor
public class SubDisConnectInformEntity implements Codec<SubDisConnectInformEntity> {

	private BaseEntity base;
	/**
	 * 错误代码：定义如下 0x00：无法连接下级平台指定的服务 IP 与端口； 0x01：上级平台客户端与下级平台服务端断开； 0x02：其他原因
	 */
	private byte reasonCode;

	@Override
	public SubDisConnectInformEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.reasonCode = msgbody.getByte(0);

		return this;
	}

	public static SubDisConnectInformEntity create(byte reasonCode) {
		SubDisConnectInformEntity baseMsg = new SubDisConnectInformEntity();

		baseMsg.reasonCode = reasonCode;

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.DOWN_DISCONNECT_INFORM,
				new byte[] { baseMsg.reasonCode });
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

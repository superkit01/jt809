package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 主链路断开通知消息
 * <p>
 * 业务数据类型标识:UP_DISCONNECT_INFORM
 * <p>
 * 当主链路中断后，下级平台可通过从链路向上级平台发送本消息通知上级平台主链路中断，本条消息无需被通知方应答
 */
@Setter
@Getter
@NoArgsConstructor
public class MainDisConnectInformEntity implements Codec<MainDisConnectInformEntity> {

	private BaseEntity base;
	/**
	 * 错误代码：定义如下 0x00:主链路断开； 0x01：其他原因
	 */
	private byte errorCode;

	@Override
	public MainDisConnectInformEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.errorCode = msgbody.getByte(0);

		return this;
	}

	public static MainDisConnectInformEntity create(byte errorCode) {
		MainDisConnectInformEntity baseMsg = new MainDisConnectInformEntity();

		baseMsg.errorCode = errorCode;

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_DISCONNECT_INFORM,
				new byte[] { baseMsg.errorCode });
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

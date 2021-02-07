package com.superkit.jt809.entity.linkManager;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 从链路连接应答信息
 * <p>
 * 业务数据类型标识:DOWN_CONNNECT_RSP
 * <p>
 * 下级平台作为服务器端向上级平台客户端返回从链路连接应答消息，上级平台在接收到该应答消息结果后，根据结果进行链路连接处理
 */
@Setter
@Getter
@NoArgsConstructor
public class SubConnectRspEntity implements Codec<SubConnectRspEntity> {

	private BaseEntity base;
	/**
	 * 验证结果：定义如下 0x00:成功； 0x01：VERIFY_CODE 错误 0x02：资源紧张，稍后再连接（已经占用） 0x03：其他
	 */
	private byte result;

	@Override
	public SubConnectRspEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.result = msgbody.getByte(0);

		return this;
	}

	public static SubConnectRspEntity create(byte result) {
		SubConnectRspEntity baseMsg = new SubConnectRspEntity();

		baseMsg.result = result;

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.DOWN_CONNECT_RSP,
				new byte[] { baseMsg.result });
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

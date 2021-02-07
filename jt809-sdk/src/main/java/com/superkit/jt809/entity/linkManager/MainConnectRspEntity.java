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
 * 主链路登录应答消息
 * <p>
 * 业务数据类型标识:UP_CONNECT_RSP
 * <p>
 * 上级平台对下级平台登录请求信息、进行安全验证后，返回相应的验证结果
 */
@Setter
@Getter
@NoArgsConstructor
public class MainConnectRspEntity implements Codec<MainConnectRspEntity> {

	private BaseEntity base;
	/**
	 * 验证结果，定义如下： 0x00:成功; 0x01:IP 地址不正确； 0x02:接入码不正确； 0x03:用户没用注册； 0x04:密码错误;
	 * 0x05:资源紧张，稍后再连接(已经占用）； 0x06：其他。
	 */
	private byte result; // 验证结果 byte
	private long verifyCode; // 校验码 byte[4]

	@Override
	public MainConnectRspEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.result = msgbody.getByte(0);
		this.verifyCode = BufUtil.buf2UnsignedLong(msgbody.slice(1, 4));

		return this;
	}

	public static MainConnectRspEntity create(byte result, long verifyCode) {
		MainConnectRspEntity baseMsg = new MainConnectRspEntity();

		baseMsg.result = result;
		baseMsg.verifyCode = verifyCode;

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_CONNECT_RSP,
				ByteUtil.combineByteArray(new byte[] { baseMsg.result }, ByteUtil.int2Byte((int) baseMsg.verifyCode)));
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

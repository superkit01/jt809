package com.superkit.jt809.entity.linkManager;

import java.nio.charset.Charset;

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
 * 主链路注销请求消息
 * <p>
 * 业务数据类型标识：UP_DISCONNECT_REQ
 * <p>
 * 下级平台在中断与上级平台的主链路连接时，应向上级平台发送主链路注销请求消息
 */
@Setter
@Getter
@NoArgsConstructor
public class MainDisConnectReqEntity implements Codec<MainDisConnectReqEntity> {

	private BaseEntity base;
	private long userId; // 用户名 byte[4]
	private String password; // 密码 byte[8]

	@Override
	public MainDisConnectReqEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.userId = BufUtil.buf2UnsignedLong(msgbody.slice(0, 4));
		this.password = new String(BufUtil.bufToBytes(msgbody.slice(4, 8)), Charset.forName("GBK"));

		return this;
	}

	public static MainDisConnectReqEntity create(long userId, String password) {
		MainDisConnectReqEntity baseMsg = new MainDisConnectReqEntity();

		baseMsg.userId = userId;
		baseMsg.password = password;

		byte[] passwordBytes = new byte[8];
		byte[] passwordTmp = password.getBytes(Charset.forName("GBK"));
		System.arraycopy(passwordTmp, 0, passwordBytes, 4, passwordTmp.length > 8 ? 8 : passwordTmp.length);

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_DISCONNECT_REQ,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) baseMsg.userId), passwordBytes));
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

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
 * 主链路登录请求消息
 * <p>
 * 业务数据类型标识: UP_CONNECT_REQ
 * <p>
 * 下级平台向上级平台发送用户名和密码等登录信息。
 */
@Setter
@Getter
@NoArgsConstructor
public class MainConnectReqEntity implements Codec<MainConnectReqEntity> {

	private BaseEntity base;

	private long userId; // 用户名 byte[4]
	private String password; // 密码 byte[8]
	private String downLinkIp; // 下级平台提供对应的从链路服务端 IP 地址 byte[32]
	private int downLinkPort; // 下级平台提供对应的从链路服务器端口号 byte[2]

	@Override
	public MainConnectReqEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.userId = BufUtil.buf2UnsignedLong(msgbody.slice(0, 4));
		this.password = new String(BufUtil.bufToBytes(msgbody.slice(4, 8)),Charset.forName("GBK"));
		this.downLinkIp = new String(BufUtil.bufToBytes(msgbody.slice(12, 32)),Charset.forName("GBK"));
		this.downLinkPort = BufUtil.buf2UnsignedInt(msgbody.slice(44, 2));

		return this;
	}

	public static MainConnectReqEntity create(long userId, String password, String downLinkIp, int downLinkPort) {
		MainConnectReqEntity baseMsg = new MainConnectReqEntity();

		baseMsg.userId = userId;
		baseMsg.password = password;
		baseMsg.downLinkIp = downLinkIp;
		baseMsg.downLinkPort = downLinkPort;

		byte[] passwordBytes = new byte[8];
		byte[] passwordTmp = password.getBytes(Charset.forName("GBK"));
		System.arraycopy(passwordTmp, 0, passwordBytes, 4, passwordTmp.length > 8 ? 8 : passwordTmp.length);

		byte[] downLinkIpBytes = new byte[32];
		byte[] downLinkIpTmp = downLinkIp.getBytes(Charset.forName("GBK"));
		System.arraycopy(downLinkIpTmp, 0, downLinkIpBytes, 12, downLinkIpTmp.length > 32 ? 32 : downLinkIpTmp.length);

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_CONNECT_REQ,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) baseMsg.userId), passwordBytes, downLinkIpBytes,
						ByteUtil.short2Bytes((short) baseMsg.downLinkPort)));
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

package com.superkit.jt809.entity.ctrlMsg;

import java.nio.charset.Charset;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 车辆应急接入监管平台请求消息 子业务类型标识:DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ
 * 描述:发生应急情况时，政府监管平台需要及时监控该车辆时，就向该车辆归属的下级平台发送该命令
 */
@Setter
@Getter
@NoArgsConstructor
public class EmergencyMonitoringReqEntity implements Codec<EmergencyMonitoringReqEntity> {
	private BaseCtrlMsgEntity baseMsg;

	private String authenicationCode; // 监管平台下发的鉴权码，用于车载终端连接到监管平台鉴权码时使用 byte[10]
	private String accessPointName; // 拨号名称，一般为服务器的APN无线通信号拨号访问点，若网络制式为 CDMA，则该值为 PPP 连接拨号号码 byte[20]
	private String userName; // 拨号用户名，服务器无线通信拨号用户名 byte[49]
	private String password; // 拨号密码，服务器无线通信拨号密码 byte[22]
	private String serverIp; // 地址，服务器IP地址或域名 byte[32]
	private int tcpPort; // 服务器TCP端口 byte[2]
	private int udpPort; // 服务器UDP端口 byte[2]
	private long endTime; // 结束时间，用 UTC 时间标识，0表示一直连接指定的服务器 byte[8]

	@Override
	public EmergencyMonitoringReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.authenicationCode = new String(BufUtil.bufToBytes(baseMsg.getData().slice(0, 10)));
		this.accessPointName = new String(BufUtil.bufToBytes(baseMsg.getData().slice(10, 20)));
		this.userName = new String(BufUtil.bufToBytes(baseMsg.getData().slice(30, 49)));
		this.password = new String(BufUtil.bufToBytes(baseMsg.getData().slice(79, 22)));
		this.serverIp = new String(BufUtil.bufToBytes(baseMsg.getData().slice(101, 32)));
		this.tcpPort = BufUtil.buf2UnsignedInt(baseMsg.getData().slice(133, 2));
		this.udpPort = BufUtil.buf2UnsignedInt(baseMsg.getData().slice(135, 2));
		this.endTime = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(137, 8));

		return this;
	}

	public static EmergencyMonitoringReqEntity create(String vehicleNo, byte vehicleColor, String authenicationCode,
			String accessPointName, String userName, String password, String serverIp, int tcpPort, int udpPort,
			long endTime) {
		EmergencyMonitoringReqEntity entity = new EmergencyMonitoringReqEntity();
		entity.authenicationCode = authenicationCode;
		entity.accessPointName = accessPointName;
		entity.userName = userName;
		entity.password = password;
		entity.serverIp = serverIp;
		entity.tcpPort = tcpPort;
		entity.udpPort = udpPort;
		entity.endTime = endTime;

		byte[] authenicationCodeBytes = new byte[10];
		byte[] authenicationCodeTmp = authenicationCode.getBytes(Charset.forName("GBK"));
		System.arraycopy(authenicationCodeTmp, 0, authenicationCodeBytes, 0,
				authenicationCodeTmp.length > 10 ? 0 : authenicationCodeTmp.length);

		byte[] accessPointNameBytes = new byte[20];
		byte[] accessPointNameTmp = accessPointName.getBytes(Charset.forName("GBK"));
		System.arraycopy(accessPointNameTmp, 0, accessPointNameBytes, 0,
				accessPointNameTmp.length > 20 ? 0 : accessPointNameTmp.length);

		byte[] userNameBytes = new byte[49];
		byte[] userNameTmp = userName.getBytes(Charset.forName("GBK"));
		System.arraycopy(userNameTmp, 0, userNameBytes, 0, userNameTmp.length > 49 ? 0 : userNameTmp.length);

		byte[] passwordBytes = new byte[22];
		byte[] passwordTmp = password.getBytes(Charset.forName("GBK"));
		System.arraycopy(passwordTmp, 0, passwordBytes, 0, passwordTmp.length > 22 ? 0 : passwordTmp.length);

		byte[] serverIpBytes = new byte[32];
		byte[] serverIpTmp = serverIp.getBytes(Charset.forName("GBK"));
		System.arraycopy(serverIpTmp, 0, serverIpBytes, 0, serverIpTmp.length > 32 ? 0 : serverIpTmp.length);

		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ,
				ByteUtil.combineByteArray(authenicationCodeBytes, accessPointNameBytes, userNameBytes, passwordBytes,
						serverIpBytes, ByteUtil.short2Bytes((short) tcpPort), ByteUtil.short2Bytes((short) udpPort),
						ByteUtil.long2Bytes(endTime)));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

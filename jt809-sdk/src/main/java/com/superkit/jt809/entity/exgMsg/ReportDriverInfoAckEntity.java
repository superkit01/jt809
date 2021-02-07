package com.superkit.jt809.entity.exgMsg;

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
 * 链路类型:主链路 上报驾驶员身份识别信息应答消息 子业务类型标识:UP_EXG_MSG_REPORT_DRIVER_INFO_ACK
 * 描述:下级平台应答上级平台发送的上报驾驶员身份识别信息请求消息，上传指定车辆的驾驶员身份识别信息数据
 */
@Setter
@Getter
@NoArgsConstructor
public class ReportDriverInfoAckEntity implements Codec<ReportDriverInfoAckEntity> {
	private BaseExgMsgEntity baseMsg;

	private String driverName; // 驾驶员姓名 byte[16]
	private String driverId; // 身份证编号 byte[20]
	private String licence; // 从业资格证（备用） byte[40]
	private String orgName; // 发证机构名称（备用） byte[200]

	@Override
	public ReportDriverInfoAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.driverName = new String(BufUtil.bufToBytes(baseMsg.getData().slice(0, 16)), Charset.forName("GBK"));
		this.driverId = new String(BufUtil.bufToBytes(baseMsg.getData().slice(16, 20)), Charset.forName("GBK"));
		this.licence = new String(BufUtil.bufToBytes(baseMsg.getData().slice(36, 40)), Charset.forName("GBK"));
		this.driverId = new String(BufUtil.bufToBytes(baseMsg.getData().slice(76, 200)), Charset.forName("GBK"));
		return this;
	}

	public static ReportDriverInfoAckEntity create(String vehicleNo, byte vehicleColor, String driverName,
			String driverId, String licence, String orgName) {
		ReportDriverInfoAckEntity entity = new ReportDriverInfoAckEntity();
		entity.driverName = driverName;
		entity.driverId = driverId;
		entity.licence = licence;
		entity.orgName = orgName;

		byte[] driverNameBytes = new byte[16];
		byte[] driverNameTmp = driverName.getBytes(Charset.forName("GBK"));
		System.arraycopy(driverNameTmp, 0, driverNameBytes, 0, driverNameTmp.length > 16 ? 16 : driverNameTmp.length);

		byte[] driverIdBytes = new byte[20];
		byte[] driverIdTmp = driverId.getBytes(Charset.forName("GBK"));
		System.arraycopy(driverIdTmp, 0, driverIdBytes, 0, driverIdTmp.length > 20 ? 20 : driverIdTmp.length);

		byte[] licenceBytes = new byte[40];
		byte[] licenceTmp = licence.getBytes(Charset.forName("GBK"));
		System.arraycopy(licenceTmp, 0, licenceBytes, 0, licenceTmp.length > 40 ? 40 : licenceTmp.length);

		byte[] orgNameBytes = new byte[200];
		byte[] orgNameTmp = orgName.getBytes(Charset.forName("GBK"));
		System.arraycopy(orgNameTmp, 0, orgNameBytes, 0, orgNameTmp.length > 200 ? 200 : orgNameTmp.length);

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_REPORT_DRIVER_INFO_ACK,
				ByteUtil.combineByteArray(driverNameBytes, driverIdBytes, licenceBytes, orgNameBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

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
 * 链路类型:主链路 上传车辆注册信息消息 子业务类型标识:UP_EXG_MSG_REGISTER
 * 描述:监控平台收到车载终端鉴权信息后，启动本命令向上级监管平台上传该车辆注册信息.各级监管平台再逐级向上级平台上传该信息
 */
@Setter
@Getter
@NoArgsConstructor
public class RegisterEntity implements Codec<RegisterEntity> {
	private BaseExgMsgEntity baseMsg;

	private String plateFormId; // 平台唯一编码 byte[11]
	private String producerId; // 车载终端厂商唯一编码 byte[11]
	private String terminalModelType; // 车载终端型号，不是 8 位时以“\0”终结 byte[8]
	private String terminalId; // 车载终端编号，大写字母和数字组成 byte[7]
	private String terminalSimCode; // 车载终端 SIM 卡电话号码。号码不是12位，则在前补充数字0 byte[12]

	@Override
	public RegisterEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.plateFormId = new String(BufUtil.bufToBytes(baseMsg.getData().slice(0, 11)), Charset.forName("GBK"));
		this.producerId = new String(BufUtil.bufToBytes(baseMsg.getData().slice(11, 11)), Charset.forName("GBK"));
		this.terminalModelType = new String(BufUtil.bufToBytes(baseMsg.getData().slice(22, 8)), Charset.forName("GBK"));
		this.terminalId = new String(BufUtil.bufToBytes(baseMsg.getData().slice(30, 7)), Charset.forName("GBK"));
		this.terminalSimCode = new String(BufUtil.bufToBytes(baseMsg.getData().slice(37, 12)), Charset.forName("GBK"));
		return this;
	}

	public static RegisterEntity create(String vehicleNo, byte vehicleColor, String plateFormId, String producerId,
			String terminalModelType, String terminalId, String terminalSimCode) {
		RegisterEntity entity = new RegisterEntity();
		entity.plateFormId = plateFormId;
		entity.producerId = producerId;
		entity.terminalModelType = terminalModelType;
		entity.terminalId = terminalId;
		entity.terminalSimCode = terminalSimCode;

		byte[] plateFormIdBytes = new byte[11];
		byte[] plateFormIdTmp = plateFormId.getBytes(Charset.forName("GBK"));
		System.arraycopy(plateFormIdTmp, 0, plateFormIdBytes, 0,
				plateFormIdTmp.length > 11 ? 11 : plateFormIdTmp.length);

		byte[] producerIdBytes = new byte[11];
		byte[] producerIdTmp = producerId.getBytes(Charset.forName("GBK"));
		System.arraycopy(producerIdTmp, 0, producerIdBytes, 0, producerIdTmp.length > 11 ? 11 : producerIdTmp.length);

		byte[] terminalModelTypeBytes = new byte[8];
		byte[] terminalModelTypeTmp = terminalModelType.getBytes(Charset.forName("GBK"));
		System.arraycopy(terminalModelTypeTmp, 0, terminalModelTypeBytes, 0,
				terminalModelTypeTmp.length > 8 ? 8 : terminalModelTypeTmp.length);

		byte[] terminalIdBytes = new byte[7];
		byte[] terminalIdTmp = terminalId.getBytes(Charset.forName("GBK"));
		System.arraycopy(terminalIdTmp, 0, terminalIdBytes, 0, terminalIdTmp.length > 7 ? 7 : terminalIdTmp.length);

		byte[] terminalSimCodeBytes = new byte[12];
		byte[] terminalSimCodeTmp = terminalSimCode.getBytes(Charset.forName("GBK"));
		System.arraycopy(terminalSimCodeTmp, 0, terminalSimCodeBytes, 0,
				terminalSimCodeTmp.length > 12 ? 12 : terminalSimCodeTmp.length);

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_REGISTER, ByteUtil.combineByteArray(plateFormIdBytes,
						producerIdBytes, terminalModelTypeBytes, terminalIdBytes, terminalSimCodeBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

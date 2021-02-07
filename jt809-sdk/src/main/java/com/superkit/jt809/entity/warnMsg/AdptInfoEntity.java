package com.superkit.jt809.entity.warnMsg;

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
 * 链路类型:主链路 上报报警信息消息 子业务类型标识:UP_WARN_MSG_ADPT_INFO
 * 描述:下级平台向上级平台上报某车辆的报警信息,本条消息上级平台无需应答
 */
@Setter
@Getter
@NoArgsConstructor
public class AdptInfoEntity implements Codec<AdptInfoEntity> {
	private BaseWarnMsgEntity baseMsg;

	/**
	 * 报警信息来源，定义如下： 0x01：车载终端 0x02：企业监控平台 0x03：政府监管平台 0x09：其他
	 */
	private byte warnSrc; // 报警信息来源 byte[1]
	private int warnType; // 报警类型 byte[2]
	private long warnTime; // 报警时间 byte[8]
	private long infoId; // 信息ID byte[4]
	private long infoLength; // 报警信息长度，最长 1024 字节 byte[4]
	private String infoContent; // 上报报警信息内容， byte[infoLength]

	@Override
	public AdptInfoEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseWarnMsgEntity().decode(buf);
		this.warnSrc = baseMsg.getData().getByte(0);
		this.warnType = BufUtil.buf2UnsignedInt(baseMsg.getData().slice(1, 2));
		this.warnTime = BufUtil.buf2SignedLong(baseMsg.getData().slice(3, 8));
		this.infoId = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(11, 4));
		this.infoLength = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(15, 4));
		this.infoContent = new String(
				BufUtil.bufToBytes(baseMsg.getData().slice(19, baseMsg.getData().readableBytes() - 19)));
		return this;
	}

	public static AdptInfoEntity create(String vehicleNo, byte vehicleColor, byte warnSrc, int warnType, long warnTime,
			long infoId, String infoContent) {
		AdptInfoEntity entity = new AdptInfoEntity();
		entity.warnSrc = warnSrc;
		entity.warnType = warnType;
		entity.warnTime = warnTime;
		entity.infoId = infoId;
		byte[] infoContentBytes = infoContent.getBytes(Charset.forName("GBK"));
		entity.infoLength = infoContentBytes.length;
		entity.infoContent = infoContent;

		entity.baseMsg = BaseWarnMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_WARN_MSG_ADPT_INFO,
				ByteUtil.combineByteArray(new byte[] { entity.warnSrc }, ByteUtil.short2Bytes((short) entity.warnType),
						ByteUtil.long2Bytes(entity.warnTime), ByteUtil.int2Byte((int) entity.infoId),
						ByteUtil.int2Byte((int) entity.infoLength), infoContentBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

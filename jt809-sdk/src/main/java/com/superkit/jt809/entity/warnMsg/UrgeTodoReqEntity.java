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
 * 链路类型:从链路 报警督办请求消息 子业务类型标识:DOWN_WARN_MSG_URGE_TODO_REQ
 * 描述:上级平台向车辆归属下级平台下发本消息，催促其及时处理相关车辆的报警信息
 */
@Setter
@Getter
@NoArgsConstructor
public class UrgeTodoReqEntity implements Codec<UrgeTodoReqEntity> {
	private BaseWarnMsgEntity baseMsg;

	private byte warnSrc; // 报警信息来源 byte[1]
	private int warnType; // 报警类型 byte[2]
	private long warnTime; // 报警时间 byte[8]
	private long supervisionId; // 报警督办ID byte[4]
	private long supervisionEndTime; // 督办截止时间，UTC 时间格式 byte[8]

	/**
	 * 督办级别，定义如下： 0x00：紧急 0x01：一般
	 */
	private byte supervisionLevel; // 督办级别 byte[1]
	private String supervisor; // 督办人 byte[16]
	private String supervisionTel; // 督办联系电话 byte[20]
	private String supervisionEmail; // 督办联系电子邮件 byte[30]

	@Override
	public UrgeTodoReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseWarnMsgEntity().decode(buf);
		this.warnSrc = baseMsg.getData().getByte(0);
		this.warnType = BufUtil.buf2UnsignedInt(baseMsg.getData().slice(1, 2));
		this.warnTime = BufUtil.buf2SignedLong(baseMsg.getData().slice(3, 8));
		this.supervisionId = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(11, 4));
		this.supervisionEndTime = BufUtil.buf2SignedLong(baseMsg.getData().slice(15, 8));
		this.supervisionLevel = baseMsg.getData().getByte(23);
		this.supervisor = new String(BufUtil.bufToBytes(baseMsg.getData().slice(24, 16)));
		this.supervisionTel = new String(BufUtil.bufToBytes(baseMsg.getData().slice(40, 20)));
		this.supervisionEmail = new String(BufUtil.bufToBytes(baseMsg.getData().slice(60, 30)));

		return this;
	}

	public static UrgeTodoReqEntity create(String vehicleNo, byte vehicleColor, byte warnSrc, int warnType,
			long warnTime, long supervisionId, long supervisionEndTime, byte supervisionLevel, String supervisor,
			String supervisionTel, String supervisionEmail) {
		UrgeTodoReqEntity entity = new UrgeTodoReqEntity();

		entity.warnSrc = warnSrc;
		entity.warnType = warnType;
		entity.warnTime = warnTime;
		entity.supervisionId = supervisionId;
		entity.supervisionEndTime = supervisionEndTime;
		entity.supervisionLevel = supervisionLevel;

		entity.supervisor = supervisor;
		entity.supervisionTel = supervisionTel;
		entity.supervisionEmail = supervisionEmail;

		byte[] supervisorBytes = new byte[16];
		byte[] supervisorTmp = supervisor.getBytes(Charset.forName("GBK"));
		System.arraycopy(supervisorTmp, 0, supervisorBytes, 0, supervisorTmp.length > 16 ? 16 : supervisorTmp.length);

		byte[] supervisionTelBytes = new byte[20];
		byte[] supervisionTelTmp = supervisionTel.getBytes(Charset.forName("GBK"));
		System.arraycopy(supervisionTelTmp, 0, supervisionTelBytes, 0,
				supervisionTelTmp.length > 20 ? 20 : supervisionTelTmp.length);

		byte[] supervisionEmailBytes = new byte[30];
		byte[] supervisionEmailBytesTmp = supervisionEmail.getBytes(Charset.forName("GBK"));
		System.arraycopy(supervisionEmailBytesTmp, 0, supervisionEmailBytes, 0,
				supervisionEmailBytesTmp.length > 30 ? 30 : supervisionEmailBytesTmp.length);

		entity.baseMsg = BaseWarnMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_WARN_MSG_URGE_TODO_REQ,
				ByteUtil.combineByteArray(new byte[] { entity.warnSrc }, ByteUtil.short2Bytes((short) entity.warnType),
						ByteUtil.long2Bytes(entity.warnTime), ByteUtil.int2Byte((int) entity.supervisionId),
						ByteUtil.long2Bytes(entity.supervisionEndTime), new byte[] { entity.supervisionLevel },
						supervisorBytes, supervisionTelBytes, supervisionEmailBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

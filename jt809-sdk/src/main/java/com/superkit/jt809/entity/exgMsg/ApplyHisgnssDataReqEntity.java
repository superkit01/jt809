package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路 补发车辆定位信息请求消息 子业务类型标识:UP_EXG_MSG_APPLY_HISGNSSDATA_REQ
 * 描述:在平台间传输链路中断并重新建立连接后，下级平台向上级平台请求中断期间内上级平台需交换至下级平台的车辆定位信息时，向上级平台发出补发车辆定位信息请求，上级平台对请求应答后进行“补发车辆定位信息”
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplyHisgnssDataReqEntity implements Codec<ApplyHisgnssDataReqEntity> {
	private BaseExgMsgEntity baseMsg;

	private long startTime; // 开始时间，用 UTC 时间表示 byte[8]
	private long endTime; // 结束时间，用 UTC 时间表示 byte[8]

	@Override
	public ApplyHisgnssDataReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.startTime = BufUtil.buf2SignedLong(baseMsg.getData().slice(0, 8));
		this.endTime = BufUtil.buf2SignedLong(baseMsg.getData().slice(8, 8));
		return this;
	}

	public static ApplyHisgnssDataReqEntity create(String vehicleNo, byte vehicleColor, long startTime, long endTime) {
		ApplyHisgnssDataReqEntity entity = new ApplyHisgnssDataReqEntity();
		entity.startTime = startTime;
		entity.endTime = endTime;

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_APPLY_HISGNSSDATA_REQ,
				ByteUtil.combineByteArray(ByteUtil.long2Bytes(entity.startTime), ByteUtil.long2Bytes(entity.endTime)));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

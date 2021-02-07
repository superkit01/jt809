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
 * 链路类型:主链路 申请交换指定车辆定位信息请求消息 子业务类型标识:UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP
 * 描述:当下级平台需要在特定时问段内监控特殊车辆时，可上传此命令到上级平台申请对该车辆定位数据交换到下级平台，申请成功后，此车辆定位数据将在指定时间内交换到该平台(即使该车没有进入该平台所属区域也会交换)
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplyForMonitorStartupEntity implements Codec<ApplyForMonitorStartupEntity> {
	private BaseExgMsgEntity baseMsg;


	private long startTime; // 开始时间，用 UTC 时间表示 byte[8]
	private long endTime; // 结束时间，用 UTC 时间表示 byte[8]



	@Override
	public ApplyForMonitorStartupEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.startTime=BufUtil.buf2SignedLong(baseMsg.getData().slice(0, 8));
		this.endTime=BufUtil.buf2SignedLong(baseMsg.getData().slice(8, 8));
		return this;
	}

	public static ApplyForMonitorStartupEntity create(String vehicleNo, byte vehicleColor, long startTime, long endTime) {
		ApplyForMonitorStartupEntity entity = new ApplyForMonitorStartupEntity();
		entity.startTime = startTime;
		entity.endTime = endTime;

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP, ByteUtil.combineByteArray(ByteUtil.long2Bytes(entity.startTime), ByteUtil.long2Bytes(entity.endTime)));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}


}

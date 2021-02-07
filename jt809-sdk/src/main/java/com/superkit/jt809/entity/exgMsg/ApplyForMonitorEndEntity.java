package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路 取消交换指定车辆定位信息请求 子业务类型标识:UP_EXG_MSG_APPLY_FOR_MONITOR_END
 * 描述:下级平台上传该命令给上级平台，取消之前申请监控的特殊车辆
 */
@Setter
@Getter
@NoArgsConstructor
public class ApplyForMonitorEndEntity implements Codec<ApplyForMonitorEndEntity> {
	private BaseExgMsgEntity baseMsg;

	@Override
	public ApplyForMonitorEndEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		return this;
	}

	public static ApplyForMonitorEndEntity create(String vehicleNo, byte vehicleColor) {
		ApplyForMonitorEndEntity entity = new ApplyForMonitorEndEntity();

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_APPLY_FOR_MONITOR_END, new byte[0]);
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

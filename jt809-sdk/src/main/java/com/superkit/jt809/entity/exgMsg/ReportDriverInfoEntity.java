package com.superkit.jt809.entity.exgMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 上报驾驶员身份识别信息请求消息 子业务类型标识:DOWN_EXG_MSG_REPORT_DRIVER_INFO
 * 描述:上级平台向下级平台下发上报车辆驾驶员身份识别信息
 */
@Setter
@Getter
@NoArgsConstructor
public class ReportDriverInfoEntity implements Codec<ReportDriverInfoEntity> {
	private BaseExgMsgEntity baseMsg;

	@Override
	public ReportDriverInfoEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		return this;
	}

	public static ReportDriverInfoEntity create(String vehicleNo, byte vehicleColor) {
		ReportDriverInfoEntity entity = new ReportDriverInfoEntity();

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_REPORT_DRIVER_INFO, new byte[0]);
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

package com.superkit.jt809.entity.ctrlMsg;

import java.nio.charset.Charset;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 车辆单向监听请求消息 子业务类型标识:DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ
 * 描述:上级平台向下级平台下发车辆单向监听清求消息
 */
@Setter
@Getter
@NoArgsConstructor
public class MonitorVehicleReqEntity implements Codec<MonitorVehicleReqEntity> {
	private BaseCtrlMsgEntity baseMsg;

	private String monitorTel; // 同拨电话号码，参数为电话号码，如有分机号码，中间用“_”分隔 byte[20]

	@Override
	public MonitorVehicleReqEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.monitorTel = new String(BufUtil.bufToBytes(baseMsg.getData()), Charset.forName("GBK"));
		return this;
	}

	public static MonitorVehicleReqEntity create(String vehicleNo, byte vehicleColor, String monitorTel) {
		MonitorVehicleReqEntity entity = new MonitorVehicleReqEntity();
		entity.monitorTel = monitorTel;

		byte[] monitorTelBytes = new byte[20];
		byte[] monitorTelTmp = monitorTel.getBytes(Charset.forName("GBK"));
		System.arraycopy(monitorTelTmp, 0, monitorTelBytes, 0, monitorTelTmp.length > 20 ? 20 : monitorTelTmp.length);

		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ, monitorTelBytes);
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

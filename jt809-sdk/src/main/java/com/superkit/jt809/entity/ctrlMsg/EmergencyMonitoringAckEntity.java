package com.superkit.jt809.entity.ctrlMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路
 * 车辆应急接入监管平台应答消息
 * 子业务类型标识:UP_CTRL_MSG_EMERGENCY_MONITORING_ACK
 * 描述:下级平台应答上级平台下发的车辆应急接入监管平台请求消息应答
 */
@Setter
@Getter
@NoArgsConstructor
public class EmergencyMonitoringAckEntity implements Codec<EmergencyMonitoringAckEntity>  {
	private BaseCtrlMsgEntity baseMsg;

    /**
     * 车辆应急接入监管结果
     * 0x00：车载终端成功收到该命令；
     * 0x01：无该车辆
     * 0x02：其他原因失败
     */
    private byte result; //车辆行驶记录数据体长度  byte[1]


	@Override
	public EmergencyMonitoringAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.result = baseMsg.getData().readByte();
		return this;
	}

	public static EmergencyMonitoringAckEntity create(String vehicleNo, byte vehicleColor, byte result) {
		EmergencyMonitoringAckEntity entity = new EmergencyMonitoringAckEntity();
		entity.result = result;
		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_CTRL_MSG_EMERGENCY_MONITORING_ACK,
				new byte[] {entity.result});
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

    

}

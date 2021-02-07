package com.superkit.jt809.entity.ctrlMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路
 * 车辆单向监听应答消息
 * 子业务类型标识:UP_CTRL_MSG_MONITOR_VEHICLE_ACK
 * 描述:下级平台向上级平台上传车辆单向监听请求消息的应答
 */
@Setter
@Getter
@NoArgsConstructor
public class MonitorVehicleAckEntity implements Codec<MonitorVehicleAckEntity> {
	private BaseCtrlMsgEntity baseMsg;

    /**
     * 车辆单向监听应答结果，定义如下：
     * 0x00：监听成功
     * 0x01：监听失败
     */
    private byte result; //  byte[1]


    @Override
	public MonitorVehicleAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.result = baseMsg.getData().readByte();
		return this;
	}

	public static MonitorVehicleAckEntity create(String vehicleNo, byte vehicleColor, byte result) {
		MonitorVehicleAckEntity entity = new MonitorVehicleAckEntity();
		entity.result = result;
		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_CTRL_MSG_MONITOR_VEHICLE_ACK,
				new byte[] {entity.result});
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}


}

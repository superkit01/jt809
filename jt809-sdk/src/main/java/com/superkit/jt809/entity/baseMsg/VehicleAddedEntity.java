package com.superkit.jt809.entity.baseMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:从链路 补报车辆静态信息请求消息 子业务类型标识:DOWN_BASE_MSG_VEHICLE_ADDED
 * 描述:上级平台在接收到车辆定位信息后，发现该车辆静态信息在上级平台不存在，上级平台向下级平台下发补报该车辆静态信息的请求消息
 */
@Setter
@Getter
@NoArgsConstructor
public class VehicleAddedEntity implements Codec<VehicleAddedEntity> {
	private BaseMsgEntity baseMsg;

	@Override
	public VehicleAddedEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseMsgEntity().decode(buf);
		return this;
	}

	public static VehicleAddedEntity create(String vehicleNo, byte vehicleColor) {
		VehicleAddedEntity entity = new VehicleAddedEntity();
		entity.baseMsg = BaseMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_BASE_MSG_VEHICLE_ADDED, new byte[0]);
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

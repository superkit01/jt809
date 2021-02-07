package com.superkit.jt809.entity.baseMsg;

import java.nio.charset.Charset;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 链路类型:主链路 补报车辆静态信息应答消息 子业务类型标识:UP_BASE_MSG_VEHICLE_ADDED_ACK
 * 描述:上级平台应答下级平台发送的补报车辆静态信息清求消息
 */
@Data
@NoArgsConstructor
public class VehicleAddedAckEntity implements Codec<VehicleAddedAckEntity> {
	private BaseMsgEntity baseMsg;

	private String carInfo; // byte[dataLength]

	@Override
	public VehicleAddedAckEntity decode(ByteBuf buf) {

		this.baseMsg = new BaseMsgEntity().decode(buf);
		this.carInfo = new String(BufUtil.bufToBytes(baseMsg.getData()), Charset.forName("GBK"));
		return this;
	}

	public static VehicleAddedAckEntity create(String vehicleNo, byte vehicleColor, String carInfo) {
		VehicleAddedAckEntity entity = new VehicleAddedAckEntity();
		entity.carInfo = carInfo;
		entity.baseMsg = BaseMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_BASE_MSG_VEHICLE_ADDED_ACK,
				entity.carInfo.getBytes(Charset.forName("GBK")));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

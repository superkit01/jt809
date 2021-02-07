package com.superkit.jt809.entity.exgMsg;

import java.nio.charset.Charset;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 链路类型:从链路
 * 交换车辆静态信息消息
 * 子业务类型标识:DOWN_EXG_MSG_CAR_INFO
 * 描述:在首次启动跨域车辆定位信息交换，或者以后交换过程中车辆静态信息有更新时，由上级平台向下级平台下发一次车辆静态信息。下.级平台接收后自行更新该车辆静态信息，本条消息客户端无需应答
 */
@Setter
@Getter
@NoArgsConstructor
public class CarInfoEntity implements Codec<CarInfoEntity> {
	private BaseExgMsgEntity baseMsg;

    private String carInfo; //车辆信息


    @Override
	public CarInfoEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.carInfo=new String(BufUtil.bufToBytes(baseMsg.getData()));
		return this;
	}
	
	public static CarInfoEntity create(String vehicleNo, byte vehicleColor, String carInfo) {
		CarInfoEntity entity = new CarInfoEntity();
		entity.carInfo = carInfo;

		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_EXG_MSG_CAR_INFO, carInfo.getBytes(Charset.forName("GBK")));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}


}

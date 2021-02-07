package com.superkit.jt809.entity.exgMsg;

import java.util.ArrayList;
import java.util.List;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路 车辆定位信息自动补报请求消息 子业务类型标识:UP_EXG_MSG_HISTORY_LOCATION
 * 描述:如果平台间传输链路中断，下级平台重新登录并与上级平台建立通信链路后，下级平台应将中断期间内车载终端上传的车辆定位信息自动补报到上级平台。如果系统断线期间，
 * 该车需发送的数据包条数大于5，则以每包五条进行补发，直到补发完毕。多条数据以卫星定位时间先后顺序排列。本条消息上级平台采用定量回复，即收到一定数量的数据后，即通过从链路应答数据量
 */
@Setter
@Getter
@NoArgsConstructor
public class HistoryLocationEntity implements Codec<HistoryLocationEntity> {
	private BaseExgMsgEntity baseMsg;
	private byte gnssCnt; // 该数据包里包含的卫星定位数据个数1<=gnssCnt<=5 byte[1]
	private List<byte[]> gnssData; // 卫星定位数据 byte[36]

	@Override
	public HistoryLocationEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseExgMsgEntity().decode(buf);
		this.gnssCnt = baseMsg.getData().getByte(0);
		this.gnssData = new ArrayList<>();
		for (int i = 0; i < this.gnssCnt; i++) {
			this.gnssData.add(BufUtil.bufToBytes(baseMsg.getData().slice(i * 36 + 1, 36)));
		}
		return this;
	}

	public static HistoryLocationEntity create(String vehicleNo, byte vehicleColor, List<byte[]> gnssDataList) {
		HistoryLocationEntity entity = new HistoryLocationEntity();
		entity.gnssCnt = (byte) gnssDataList.size();

		List<byte[]> gnssDataBytesList = new ArrayList<>();
		for (byte[] gnssData : gnssDataList) {
			byte[] gnssDataBytes = new byte[36];
			System.arraycopy(gnssData, 0, gnssDataBytes, 0, gnssData.length > 36 ? 36 : gnssData.length);
			gnssDataBytesList.add(gnssDataBytes);
		}
		entity.gnssData = gnssDataBytesList;

		byte[] result = new byte[entity.gnssCnt * 36];
		for (int i = 0; i < gnssDataBytesList.size(); i++) {
			System.arraycopy(gnssDataBytesList.get(i), 0, result, i * 36, 36);
		}
		entity.baseMsg = BaseExgMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_EXG_MSG_HISTORY_LOCATION,
				ByteUtil.combineByteArray(new byte[] { entity.gnssCnt }, result));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}
}

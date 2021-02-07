package com.superkit.jt809.entity.warnMsg;

import java.nio.charset.Charset;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 主链路车辆报警信息业务 链路类型:主链路 消息方向:下级平台往上级平台 业务数据类型标识:UP_WARN_MSG
 * 描述:下级平台向上级平台发送车辆报警信息业务
 * <p>
 * <p>
 * 从链路车辆报警信息业务 链路类型:从链路 消息方向:上级平台往下级平台 业务数据类型标识:DOWN_WARN_MSG
 * 描述:上级平台向下级平台发送报瞥信息业务
 */
@Setter
@Getter
@NoArgsConstructor
public class BaseWarnMsgEntity implements Codec<BaseWarnMsgEntity> {

	private BaseEntity base;

	private String vehicleNo; // 车牌号 byte[21]
	private byte vehicleColor; // 车辆颜色 byte[1]
	private int dataType; // 子业务类型标识 byte[2]
	private long dataLength; // 后续数据长度 byte[4]

	private ByteBuf data; // 数据部分 byte[dataLength]

	@Override
	public BaseWarnMsgEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.vehicleNo = new String(BufUtil.bufToBytes(msgbody.slice(0, 21)), Charset.forName("GBK"));
		this.vehicleColor = msgbody.getByte(21);
		this.dataType = BufUtil.buf2UnsignedInt(msgbody.slice(22, 2));
		this.dataLength = BufUtil.buf2UnsignedLong(msgbody.slice(24, 4));
		this.data = msgbody.slice(28, msgbody.readableBytes() - 28);

		return this;
	}

	public static BaseWarnMsgEntity create(String vehicleNo, byte vehicleColor, int dataType, byte[] data) {
		BaseWarnMsgEntity baseMsg = new BaseWarnMsgEntity();
		baseMsg.vehicleNo = vehicleNo;
		baseMsg.vehicleColor = vehicleColor;
		baseMsg.dataType = dataType;
		baseMsg.dataLength = data.length;

		byte[] msgbody = new byte[21 + 1 + 2 + 4 + (int) baseMsg.dataLength];
		byte[] vehicleBytes = vehicleNo.getBytes(Charset.forName("GBK"));
		System.arraycopy(vehicleBytes, 0, msgbody, 0, vehicleBytes.length > 21 ? 21 : vehicleBytes.length);
		msgbody[21] = vehicleColor;
		System.arraycopy(ByteUtil.short2Bytes((short) baseMsg.dataType), 0, msgbody, 22, 2);
		System.arraycopy(ByteUtil.int2Byte((int) baseMsg.dataLength), 0, msgbody, 24, 4);
		System.arraycopy(data, 0, msgbody, 28, (int) baseMsg.dataLength);

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_WARN_MSG, msgbody);
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

package com.superkit.jt809.entity.platformMsg;

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
 * 主链路平台间信息交互业务 链路类型:主链路 消息方向:下级平台往上级平台 业务数据类型标识:UP_PLATFORM_MSG
 * 描述:下级平台向上级平台发送平台间交互信息
 * <p>
 * <p>
 * 从链路平台间信息交互业务 链路类型:从链路 消息方向:上级平台往下级平台 业务数据类型标识:DOWN_PLATFORM_MSG
 * 描述:上级平台向下级平台发送平台问交互信息
 */
@Setter
@Getter
@NoArgsConstructor
public class BasePlatFormMsgEntity implements Codec<BasePlatFormMsgEntity> {

	private BaseEntity base;

	private int dataType; // 子业务类型标识 byte[2]
	private long dataLength; // 后续数据长度 byte[4]

	private ByteBuf data; // 数据部分 byte[dataLength]

	@Override
	public BasePlatFormMsgEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.dataType = BufUtil.buf2UnsignedInt(msgbody.slice(0, 2));
		this.dataLength = BufUtil.buf2UnsignedLong(msgbody.slice(2, 4));
		this.data = msgbody.slice(6, msgbody.readableBytes() - 6);

		return this;
	}

	public static BasePlatFormMsgEntity create(int dataType, byte[] data) {
		BasePlatFormMsgEntity baseMsg = new BasePlatFormMsgEntity();
		baseMsg.dataType = dataType;
		baseMsg.dataLength = data.length;

		byte[] msgbody = new byte[2 + 4 + (int) baseMsg.dataLength];
		System.arraycopy(ByteUtil.short2Bytes((short) baseMsg.dataType), 0, msgbody, 0, 2);
		System.arraycopy(ByteUtil.int2Byte((int) baseMsg.dataLength), 0, msgbody, 2, 4);
		System.arraycopy(data, 0, msgbody, 6, (int) baseMsg.dataLength);

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.UP_PLATFORM_MSG, msgbody);
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

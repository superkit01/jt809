package com.superkit.jt809.entity.warnMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路 报警督办应答消息 子业务类型标识:UP_WARN_MSG_URGE_TODO_ACK
 * 描述:下级平台应答上级平台下发的报警督办请求消息，向上级平台上报车辆的报瞥处理结果
 */
@Setter
@Getter
@NoArgsConstructor
public class UrgeTodoAckEntity implements Codec<UrgeTodoAckEntity> {
	private BaseWarnMsgEntity baseMsg;

	private long supervisionId; // 报警督办ID byte[4]
	/**
	 * 报警处理结果，定义如下： 0x00：处理中 0x01：已处理完毕 0x02：不做处理 0x03：将来处理
	 */
	private byte result; // 报警督办结果 byte[1]

	@Override
	public UrgeTodoAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseWarnMsgEntity().decode(buf);
		this.supervisionId = BufUtil.buf2UnsignedLong(baseMsg.getData().slice(0, 4));
		this.result = baseMsg.getData().getByte(4);
		return this;
	}

	public static UrgeTodoAckEntity create(String vehicleNo, byte vehicleColor, long supervisionId, byte result) {
		UrgeTodoAckEntity entity = new UrgeTodoAckEntity();
		entity.supervisionId = supervisionId;
		entity.result = result;

		entity.baseMsg = BaseWarnMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_WARN_MSG_URGE_TODO_ACK,
				ByteUtil.combineByteArray(ByteUtil.int2Byte((int) entity.supervisionId), new byte[] { entity.result }));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}

}

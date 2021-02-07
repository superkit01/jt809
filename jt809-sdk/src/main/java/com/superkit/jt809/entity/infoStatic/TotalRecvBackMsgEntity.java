package com.superkit.jt809.entity.infoStatic;

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
 * 接收车辆定位信息数量通知消息 业务数据类型标识: DOWN_TOTAL_RECV_BACK_MSG
 * 描述：上级平台向下级平台定星通知已经收到下级平台上传的车辆定位信息数量(如:每收到10,000 条车辆定位信息通知一次)
 */
@Setter
@Getter
@NoArgsConstructor
public class TotalRecvBackMsgEntity implements Codec<TotalRecvBackMsgEntity> {

	private BaseEntity base;

	private long dynamicInfoTotal; // 定位信息数量 byte[4]
	private long startTime; // 开始时间 byte[8]
	private long endTime; // 结束时间 byte[8]


	@Override
	public TotalRecvBackMsgEntity decode(ByteBuf buf) {
		this.base = new BaseEntity().decode(buf);

		ByteBuf msgbody = this.base.getMessageBody();
		this.dynamicInfoTotal = BufUtil.buf2UnsignedLong(msgbody.slice(0, 4));
		this.startTime = BufUtil.buf2SignedLong(msgbody.slice(4, 8));
		this.endTime = BufUtil.buf2SignedLong(msgbody.slice(12, 8));

		return this;
	}

	public static TotalRecvBackMsgEntity create(long dynamicInfoTotal,
			long startTime, long endTime) {
		TotalRecvBackMsgEntity baseMsg = new TotalRecvBackMsgEntity();
		
		baseMsg.dynamicInfoTotal = dynamicInfoTotal;
		baseMsg.startTime = startTime;
		baseMsg.endTime = endTime;

		byte[] msgbody = ByteUtil.combineByteArray(ByteUtil.int2Byte((int) baseMsg.dynamicInfoTotal),
				ByteUtil.long2Bytes(baseMsg.startTime), ByteUtil.long2Bytes(baseMsg.endTime));

		baseMsg.base = BaseEntity.create(MsgConstant.PrimaryServiceType.DOWN_TOTAL_RECY_BACK_MSG, msgbody);
		return baseMsg;
	}

	@Override
	public byte[] encode() {
		return base.encode();
	}

}

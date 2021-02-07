package com.superkit.jt809.entity.warnMsg;

import java.nio.charset.Charset;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 链路类型:从链路
 * 报警预警消息
 * 子业务类型标识:DOWN_WARN_MSG_EXG_INFORM
 * 描述:用于上级平台向车辆跨域目的地下级平台下发相关车辆的当前报警情况,本条消息下级平台无需应答
 */
@Setter
@Getter
@NoArgsConstructor
public class ExgInformEntity implements Codec<ExgInformEntity> {
	private BaseWarnMsgEntity baseMsg;
	
    private byte warnSrc; //报警信息来源  byte[1]
    private int warnType; //报警类型  byte[2]
    private long warnTime; //报警时间  byte[8] UTC 时间格式
    private long warnLength; //报警信息长度  byte[4]
    private String warnContent; //报警描述，信息提示  byte[warnLength]

    
    @Override
	public ExgInformEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseWarnMsgEntity().decode(buf);
		this.warnSrc = baseMsg.getData().getByte(0);
		this.warnType = BufUtil.buf2UnsignedInt(baseMsg.getData().slice(1, 2));
		this.warnTime = BufUtil.buf2SignedLong(baseMsg.getData().slice(3, 8));
        this.warnLength= BufUtil.buf2UnsignedLong(baseMsg.getData().slice(3, 8));
        this.warnContent=new String(BufUtil.bufToBytes(baseMsg.getData().slice(15, baseMsg.getData().readableBytes() - 15)));
		return this;
	}

	public static ExgInformEntity create(String vehicleNo, byte vehicleColor, byte warnSrc, int warnType,
            long warnTime,  String warnContent) {
		ExgInformEntity entity = new ExgInformEntity();
		entity.warnSrc = warnSrc;
		entity.warnType = warnType;
		entity.warnTime = warnTime;
		byte[] warnContentBytes = warnContent.getBytes(Charset.forName("GBK"));
		
		entity.warnLength = warnContentBytes.length;
		entity.warnContent = warnContent;

		entity.baseMsg = BaseWarnMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.DOWN_WARN_MSG_EXG_INFORM,ByteUtil.combineByteArray(new byte[]{entity.warnSrc}, ByteUtil.short2Bytes((short) entity.warnType),
		                ByteUtil.long2Bytes(entity.warnTime), ByteUtil.int2Byte((int)entity.warnLength),warnContentBytes));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}


}



package com.superkit.jt809.entity.ctrlMsg;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.entity.Codec;
import com.superkit.jt809.util.BufUtil;
import com.superkit.jt809.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 链路类型:主链路
 * 车辆拍照应答消息
 * 子业务类型标识:UP_CTRL_MSG_TAKE_PHOTO_ACK
 * 描述:下级平台应答上级平台发送的车辆拍照请求消息，上传图片信息到上级平台
 */
@Setter
@Getter
@NoArgsConstructor
public class TakePhotoAckEntity implements Codec<TakePhotoAckEntity> {
	private BaseCtrlMsgEntity baseMsg;
    /**
     * 拍照应答标识，标识拍照后的结果或原因,定义如下:
     * 0x00:布支持拍照相;
     * 0x01:完成拍照:
     * 0x02:完成拍照、照片数据稍后传送;
     * 0x03:未拍照(不在线);
     * 0x04:未拍照;(无法使用指定镜头);
     * 0x05:未拍照(其他原因）；
     * 0x09:车牌号码错误
     */
    private byte photoRspFlag; //拍照应答标识  byte[1]
    private byte[] gnssData; //拍照位置地点  byte[36]
    private byte lensId; //镜头ID  byte[1]
    private long photoLen; //图片长度  byte[4]

    /**
     * 图片大小，定义如下：
     * 0x01:320x240:
     * 0x02:640x48;
     * 0x03:800xG00;
     * 0x04:1024x76;
     * 0x05: l 76x 144[QCIF];
     * 0x06:352*288[CIF];
     * 0x07:704*288[HALF D1];
     * 0x08:704*576[D1]。
     */
    private byte size; //图片大小 byte[1]

    /**
     * 图像格式，定义如下：
     * 0x0l:jpg:
     * 0x02:gif;
     * 0x03:tiff;
     * 0x04:png.
     */
    private byte type; //图像格式  byte[1]
    private byte[] photoContent; //图片内容  byte[photoLen]
    

	@Override
	public TakePhotoAckEntity decode(ByteBuf buf) {
		this.baseMsg = new BaseCtrlMsgEntity().decode(buf);
		this.photoRspFlag=baseMsg.getData().getByte(0);
        this.gnssData=BufUtil.bufToBytes(baseMsg.getData().slice(1, 36));
        this.lensId=baseMsg.getData().getByte(37);
        this.photoLen= BufUtil.buf2UnsignedLong(baseMsg.getData().slice(38, 4));
        this.size=baseMsg.getData().getByte(42);
        this.type=baseMsg.getData().getByte(43);
        this.photoContent=BufUtil.bufToBytes(baseMsg.getData().slice(44, baseMsg.getData().readableBytes() - 44));
		return this;
	}

	public static TakePhotoAckEntity create(String vehicleNo, byte vehicleColor, byte photoRspFlag, byte[] gnssData, byte lensId, long photoLen, byte size, byte type, byte[] photoContent) {
		TakePhotoAckEntity entity = new TakePhotoAckEntity();
		entity.photoRspFlag = photoRspFlag;
        byte[] gnssDataBytes = new byte[36];
        System.arraycopy(gnssData, 0, gnssDataBytes, 0, gnssData.length > 36 ? 36 : gnssData.length);
        entity.gnssData = gnssDataBytes;
        entity.lensId = lensId;
        entity.photoLen = photoLen;
        entity.size = size;
        entity.type = type;
        entity.photoContent = photoContent;

		entity.baseMsg = BaseCtrlMsgEntity.create(vehicleNo, vehicleColor,
				MsgConstant.SubServiceType.UP_CTRL_MSG_TAKE_PHOTO_ACK, ByteUtil.combineByteArray(new byte[]{entity.photoRspFlag}, gnssDataBytes,
		                new byte[]{entity.lensId}, ByteUtil.int2Byte((int)entity.photoLen), new byte[]{entity.size}, new byte[]{entity.type}, entity.photoContent));
		return entity;
	}

	@Override
	public byte[] encode() {
		return baseMsg.encode();
	}


}

package com.superkit.jt809.dto;

import com.superkit.jt809.util.ByteUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 车辆定位信息数据体
 */
@Setter
@Getter
@Accessors(chain = true)
public class GnssData {
    private final int GNSS_DATA_LENGTH = 36;

    /**
     * 该字段标识传输的定位信息是否使用国家测绘局批准的地图保密插件进行加密。
     * 加密标识：1-已加密，0-未加密
     * byte[1]
     */
    private byte encrypt;

    /**
     * 日月年（dmyy），年的表示是先将年转换成两位十六进制数，如2009标识为 0x070xD9.
     * byte[4]
     */
    private byte[] date;

    /**
     * 时分秒（hms）
     * byte[3]
     */
    private byte[] time;

    /**
     * 经度，单位为 1*10^-6 度
     * byte[4]
     */
    private long lon;

    /**
     * 纬度，单位为 1*10^-6 度
     * byte[4]
     */
    private long lat;

    /**
     * 速度，指卫星定位车载终端设备上传的行车速度信息
     * byte[2]
     */
    private int vec1;

    /**
     * 行驶记录速度，指车辆行驶记录设备上传的行车速度信息，为必填项。单位为千米每小时（km/h）
     * * byte[2]
     */
    private int vec2;

    /**
     * 车辆当前总里程数，值车辆上传的行车里程数。单位为千米（km）
     * byte[4]
     */
    private long vec3;

    /**
     * 方向，0-359，单位为度（。），正北为 0，顺时针
     * byte[2]
     */
    private int direction;

    /**
     * 海拔高度，单位为米（m）
     * byte[2]
     */
    private int altitude;

    /**
     * 车辆状态，二进制表示，B31B30B29......B2B1B0,具体定义按照JT/T808-2011 中表 17 的规定
     * byte[4]
     */
    private long state;

    /**
     * 报警状态，二进制表示，0 标识正常，1 表示报警：B31B30B29......B2B1B0.具体定义按照JT/T808-2011中表18的规定
     * byte[4]
     */
    private long alarm;


    public byte[] encode() {

        byte[] gnssDataBytes = new byte[GNSS_DATA_LENGTH];
        gnssDataBytes[0] = this.encrypt;

//        //日月年
//        gnssDataBytes[1] =  this.date[0];
//        gnssDataBytes[2] =  this.date[1];
//        byte[] yearBytes = TsariByteUtil.short2Bytes((short) this.date.getYear());
        System.arraycopy( this.date, 0, gnssDataBytes, 1, 4);


//        //时分秒
//        gnssDataBytes[5] = (byte) this.time.getHour();
//        gnssDataBytes[6] = (byte) this.time.getMinute();
//        gnssDataBytes[7] = (byte) this.time.getSecond();
        
        System.arraycopy( this.time, 0, gnssDataBytes,5,3);
        //经度
        System.arraycopy(ByteUtil.int2Byte((int)this.lon), 0, gnssDataBytes, 8, 4);
        //纬度
        System.arraycopy(ByteUtil.int2Byte((int)this.lat), 0, gnssDataBytes, 12, 4);
        //行车速度
        System.arraycopy(ByteUtil.short2Bytes((short)this.vec1), 0, gnssDataBytes, 16, 2);
        //行驶记录速度
        System.arraycopy(ByteUtil.short2Bytes((short)this.vec2), 0, gnssDataBytes, 18, 2);
        //当前总里程数
        System.arraycopy(ByteUtil.int2Byte((int)this.vec3), 0, gnssDataBytes, 20, 4);
        //方向
        System.arraycopy(ByteUtil.short2Bytes((short)this.direction), 0, gnssDataBytes, 24, 2);
        //海拔高度
        System.arraycopy(ByteUtil.short2Bytes((short)this.altitude), 0, gnssDataBytes, 26, 2);
        //车辆状态
        System.arraycopy(ByteUtil.int2Byte((int)this.state), 0, gnssDataBytes, 28, 4);
        //报警状态
        System.arraycopy(ByteUtil.int2Byte((int)this.alarm), 0, gnssDataBytes, 32, 4);
        return gnssDataBytes;
    }
}

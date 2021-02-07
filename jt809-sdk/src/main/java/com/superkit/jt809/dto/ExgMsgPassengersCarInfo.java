package com.superkit.jt809.dto;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.Setter;

/**
 * 跨域车辆静态信息数据体
 * 客运
 * <p>
 * 转发跨域车辆信息格式使用字符串表示，标识与内容之问用半角"：="分开.，不同标识以半角";"为分隔符，如数据项为空，在":="后不加任何数值。表示如下:
 * 标识:=内容;标识:=内容
 */
@Setter
@Getter
public class ExgMsgPassengersCarInfo{
	   /**
     * 运输行业编码 必填
     * 按照 JT/T415-2006 中 5.2.1 的规定
     */
    private String TRANS_TYPE;

    /**
     * 车牌号 必填
     * 公安车管部门核发的机动车拍照号码:车牌照号码中不设分隔符号，所有字母数字连续保存，如:京 CJ6789,不要保存为“京C-.16789 "或“京 C . J6789 ".
     */
    private String VIN;


    /**
     * 车籍地 必填
     * 行政区划代码，按照 GB/T2260 的规定
     */
    private String VEHICLE_NATIONALITY;

    /**
     * 车辆类型 必填
     * 按照 JT/T415-2006 中 5.4.9 的规定
     */
    private String VEHICLE_TYPE;

    /**
     * 道路运输证号 必填
     * 车辆道路运输证编号
     */
    private String RTPN;


    /**
     * 业户名称 必填
     * 运输企业名称
     */
    private String OWERS_NAME;

    /**
     * 业户原 ID
     * 运输企业原 ID 编号
     */
    private String OWERS_ORIG_ID;

    /**
     * 业户联系电话
     */
    private String OWERS_TEL;

    /**
     * 经营许可证号
     * 道路运输经营许可证
     */
    private String RTOLN;

    /**
     * 车辆厂牌型号
     */
    private String VEHICLE_MODE;

    /**
     * 车辆颜色 必填
     * 按照 JT/T415-2006 中 5.4.12 的规定
     */
    private String VEHICLE_COLOR;

    /**
     * 车辆原编号 必填
     * 车辆在原系统中的 ID 编号
     */
    private String VEHICLE_ORIG_ID;

    /**
     * 驾驶员情况
     * 信息内容包括：驾驶员姓名、驾驶员从业资格证号、驾驶员手机号码、信息按以下格式填写，如有多条信息以半角“、”分隔。
     * 如：驾驶员姓名，驾驶员从业资格证号，驾驶员手机号码。
     */
    private String DRIVER_INFO;

    /**
     * 经营区域
     * 经营区域根据道路旅客运输班线 的起始点所在行政区域进行分类，按照JT/T415-2006中5.2.7 的规定。
     */
    private String BUSSINESS_AREA;

    /**
     * 班线类型
     * 班线类型按照经营区域和营运线路长度对班车客运线路进行分类，按照JT/T415-2006中5.2.8的规定
     */
    private String BANLINE_TYPE;

    /**
     * 核定座位
     */
    private String APPROED_SEATS;

    /**
     * 始发地
     */
    private String ORIGN;

    /**
     * 迄点地
     */
    private String DESTINATION;

    /**
     * 始发站
     */
    private String DEPARTURE_ST;

    /**
     * 迄点地
     */
    private String DES_ST;


    public byte[] encode(String TRANS_TYPE, String VIN, String VEHICLE_NATIONALITY, String VEHICLE_TYPE, String RTPN, String OWERS_NAME, String OWERS_ORIG_ID,
                                  String OWERS_TEL, String RTOLN, String VEHICLE_MODE, String VEHICLE_COLOR, String VEHICLE_ORIG_ID, String DRIVER_INFO, String BUSSINESS_AREA,
                                  String BANLINE_TYPE, String APPROED_SEATS, String ORIGN, String DESTINATION, String DEPARTURE_ST, String DES_ST) {
        StringBuilder sb = new StringBuilder();
        sb.append("TRANS_TYPE:=").append(TRANS_TYPE).append(";");
        sb.append("VIN:=").append(VIN).append(";");
        sb.append("VEHICLE_NATIONALITY:=").append(VEHICLE_NATIONALITY).append(";");
        sb.append("VEHICLE_TYPE:=").append(VEHICLE_TYPE).append(";");
        sb.append("RTPN:=").append(RTPN).append(";");
        sb.append("OWERS_NAME:=").append(OWERS_NAME).append(";");
        sb.append("OWERS_ORIG_ID:=").append(OWERS_ORIG_ID).append(";");
        sb.append("OWERS_TEL:=").append(OWERS_TEL).append(";");
        sb.append("RTOLN:=").append(RTOLN).append(";");
        sb.append("VEHICLE_MODE:=").append(VEHICLE_MODE).append(";");
        sb.append("VEHICLE_COLOR:=").append(VEHICLE_COLOR).append(";");
        sb.append("VEHICLE_ORIG_ID:=").append(VEHICLE_ORIG_ID).append(";");
        sb.append("DRIVER_INFO:=").append(DRIVER_INFO).append(";");
        sb.append("BUSSINESS_AREA:=").append(BUSSINESS_AREA).append(";");
        sb.append("BANLINE_TYPE:=").append(BANLINE_TYPE).append(";");
        sb.append("APPROED_SEATS:=").append(APPROED_SEATS).append(";");
        sb.append("ORIGN:=").append(ORIGN).append(";");
        sb.append("DESTINATION:=").append(DESTINATION).append(";");
        sb.append("DEPARTURE_ST:=").append(DEPARTURE_ST).append(";");
        sb.append("DES_ST:=").append(DES_ST);

        return sb.toString().getBytes(Charset.forName("GBK"));
    }


}

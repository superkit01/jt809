package com.superkit.jt809.dto;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.Setter;

/**
 * 跨域车辆静态信息数据体
 * 货运
 * <p>
 * 转发跨域车辆信息格式使用字符串表示，标识与内容之问用半角"：="分开.，不同标识以半角";"为分隔符，如数据项为空，在":="后不加任何数值。表示如下:
 * 标识:=内容;标识:=内容
 */
@Setter
@Getter
public class ExgMsgGoodsCarInfo  {
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
     * 牵引总质量  如果 VIN 为牵引车，需填写此项
     */
    private String TRACTION;

    /**
     * 挂车车牌号
     */
    private String TRAILER_VIN;


    /**
     * 押运员情况
     * <p>
     * 危险品货运车辆应填写本字段。 信息内容包括：押运员姓名、押运员从业资格证号、押运员手机号码，信息按以下格式填写，如有多条信息以半角“、”分隔。
     * 如：押运员姓名、押运员从业资格证号、押运员手机号码。
     */
    private String GUARDS_INFO;

    /**
     * 核定吨位
     * 如果车为挂车，该车填写挂车的核定吨位
     */
    private String APPROVED_TONNAGE;

    /**
     * 危险品货物分类
     * 危险品货运车辆应填写本字段，具体内容按照 JT/T415-2006 中 5.2.4 的规定
     */
    private String DG_TYPE;

    /**
     * 货物品名
     */
    private String CARGO_NAME;

    /**
     * 货物吨位
     */
    private String CARGO_TONNAGE;

    /**
     * 运输出发地
     */
    private String TRANSPORT_ORIGIN;

    /**
     * 运输目的地
     */
    private String TRANSPORT_DES;

    /**
     * TSSL 运输起止时间
     * 货物运输出发及到达时间，中间以“|”间隔。时间按以UTC值表示，如：1261486591|126488899
     */
    private String TSSL;


    public byte[] transferToBytes(String TRANS_TYPE, String VIN, String TRACTION, String TRAILER_VIN, String VEHICLE_NATIONALITY, String VEHICLE_TYPE, String RTPN, String OWERS_NAME,
                                  String OWERS_ORIG_ID, String OWERS_TEL, String RTOLN, String VEHICLE_MODE, String VEHICLE_COLOR, String VEHICLE_ORIG_ID, String DRIVER_INFO, String GUARDS_INFO,
                                  String APPROVED_TONNAGE, String DG_TYPE, String CARGO_NAME, String CARGO_TONNAGE, String TRANSPORT_ORIGIN, String TRANSPORT_DES, String TSSL) {
        StringBuilder sb = new StringBuilder();
        sb.append("TRANS_TYPE:=").append(TRANS_TYPE).append(";");
        sb.append("VIN:=").append(VIN).append(";");
        sb.append("TRACTION:=").append(TRACTION).append(";");
        sb.append("TRAILER_VIN:=").append(TRAILER_VIN).append(";");
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
        sb.append("GUARDS_INFO:=").append(GUARDS_INFO).append(";");
        sb.append("APPROVED_TONNAGE:=").append(APPROVED_TONNAGE).append(";");
        sb.append("DG_TYPE:=").append(DG_TYPE).append(";");
        sb.append("CARGO_NAME:=").append(CARGO_NAME).append(";");
        sb.append("CARGO_TONNAGE:=").append(CARGO_TONNAGE).append(";");
        sb.append("TRANSPORT_ORIGIN:=").append(TRANSPORT_ORIGIN).append(";");
        sb.append("TRANSPORT_DES:=").append(TRANSPORT_DES).append(";");
        sb.append("TSSL:=").append(TSSL);


        return sb.toString().getBytes(Charset.forName("GBK"));
    }

}

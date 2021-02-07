package com.superkit.jt809.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 车辆静态信息数据体
 * <p>
 * 车辆静态信息格式使用字符串表示，标识与内容之间用半角":="分开，不同标识以半角";"为分隔符，如数据项为空，在":="后不加任何数值。表示如下:
 * 标识:=内容;标识:=内容。
 */
@Setter
@Getter
@Accessors(chain = true)
public class BaseMsgCarInfo {

    /**
     * 车牌号 必填
     * 公安车管部门核发的机动车拍照号码:车牌照号码中不设分隔符号，所有字母数字连续保存，如:京CJ6789,不要保存为“京C-.16789 "或“京 C . J6789 ".
     */
    private String VIN;

    /**
     * 车辆颜色 必填
     * 按照 JT/T415-2006 中 5.4.12 的规定
     */
    private String VEHICLE_COLOR;

    /**
     * 车辆类型 必填
     * 按照 JT/T415-2006 中 5.4.9 的规定
     */
    private String VEHICLE_TYPE;


    /**
     * 运输行业编码 必填
     * 按照 JT/T415-2006 中 5.2.1 的规定
     */
    private String TRANS_TYPE;

    /**
     * 车籍地 必填
     * 行政区划代码，按照 GB/T2260 的规定
     */
    private String VEHICLE_NATIONALITY;

    /**
     * 业户 ID 必填
     * 该业户 ID 为下级平台存储业户信息所采用的 ID 编号
     */
    private String OWERS_ID;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VIN:=").append(this.VIN).append(";");
        sb.append("VEHICLE_COLOR:=").append(this.VEHICLE_COLOR).append(";");
        sb.append("VEHICLE_TYPE:=").append(this.VEHICLE_TYPE).append(";");
        sb.append("TRANS_TYPE:=").append(this.TRANS_TYPE).append(";");
        sb.append("VEHICLE_NATIONALITY:=").append(this.VEHICLE_NATIONALITY).append(";");
        sb.append("OWERS_ID:=").append(this.OWERS_ID).append(";");
        sb.append("OWERS_NAME:=").append(this.OWERS_NAME).append(";");
        sb.append("OWERS_ORIG_ID:=").append(this.OWERS_ORIG_ID).append(";");
        sb.append("OWERS_TEL:=").append(this.OWERS_TEL);
        return sb.toString();
    }


}

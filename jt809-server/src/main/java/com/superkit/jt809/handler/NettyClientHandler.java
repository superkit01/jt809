package com.superkit.jt809.handler;

import com.superkit.jt809.MsgConstant;
import com.superkit.jt809.NettyClient;
import com.superkit.jt809.entity.BaseEntity;
import com.superkit.jt809.entity.baseMsg.BaseMsgEntity;
import com.superkit.jt809.entity.ctrlMsg.BaseCtrlMsgEntity;
import com.superkit.jt809.entity.exgMsg.BaseExgMsgEntity;
import com.superkit.jt809.entity.platformMsg.BasePlatFormMsgEntity;
import com.superkit.jt809.entity.warnMsg.BaseWarnMsgEntity;
import com.superkit.jt809.manager.ProcessorManager;
import com.superkit.jt809.util.BufUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	private ProcessorManager processorManager;

	private NettyClient nettyClient;

	public NettyClientHandler(ProcessorManager processorManager, NettyClient nettyClient) {
		this.processorManager = processorManager;
		this.nettyClient = nettyClient;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.warn("client-exceptionCaught");
		log.error("NettyClient-exceptionCaught", cause);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("client-channelActive");
		log.info("【发送】主链路登录请求");
		processorManager.getLinkManagerProcessor().sendMainConnectReq(ctx.channel());

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		log.warn("client-userEventTriggered");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.warn("client-channelInactive");
		try {
			nettyClient.stopServer();
			nettyClient.run();
		} catch (Exception e) {
			log.error("终止当前channel异常", e);
		}

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
		try {

			ByteBuf byteBuf = (ByteBuf) msg;
			log.info("接收报文:{}", BufUtil.buf2HexStr(byteBuf));

			BaseEntity baseEntity = new BaseEntity().decode(byteBuf);

			switch (baseEntity.getHeader().getMsgId()) {
			/**
			 * 链路管理
			 */
			case MsgConstant.PrimaryServiceType.UP_CONNECT_RSP:
				log.info("【接收】主链路登录应答");
				processorManager.getLinkManagerProcessor().receiveMainConnectRsp(byteBuf, ctx.channel());
				break;
			case MsgConstant.PrimaryServiceType.UP_DISCONNECT_RSP:
				log.info("【接收】主链路注销应答");
				processorManager.getLinkManagerProcessor().receiveMainDisConnectRsp(byteBuf, ctx.channel());
				break;
			case MsgConstant.PrimaryServiceType.UP_LINKTEST_RSP:
				log.info("【接收】主链路心跳应答");
				processorManager.getLinkManagerProcessor().receiveMainLinkTestRsp(byteBuf, ctx.channel());
				break;
			case MsgConstant.PrimaryServiceType.DOWN_DISCONNECT_INFORM:
				log.info("【接收】从链路断开通知");
				processorManager.getLinkManagerProcessor().receiveSubDisConnectInform(byteBuf, ctx.channel());
				break;

			/**
			 * 信息统计
			 */
			case MsgConstant.PrimaryServiceType.DOWN_TOTAL_RECY_BACK_MSG: {
				log.info("【接收】车辆定位信息数量通知");
				processorManager.getInfoStaticProcessor().receiveTotalRecvBackMsg(byteBuf, ctx.channel());
				break;
			}
			/**
			 * 车辆动态信息交换业务
			 */
			case MsgConstant.PrimaryServiceType.DOWN_EXG_MSG: {
				BaseExgMsgEntity msgEntity = new BaseExgMsgEntity().decode(byteBuf);
				int dataType = msgEntity.getDataType();
				switch (dataType) {
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_CAR_LOCATION:
					log.info("【接收】交换车辆定位信息");
					processorManager.getExgProcessor().receiveExgMsgCarLocation(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_HISTORY_ARCOSSAREA:
					log.info("【接收】车辆定位信息交换补发");
					processorManager.getExgProcessor().receiveExgMsgHistoryArcossarea(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_CAR_INFO:
					log.info("【接收】交换车辆静态信息");
					processorManager.getExgProcessor().receiveExgMsgCarInfo(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_RETURN_STARTUP:
					log.info("【接收】启动车辆定位信息交换请求");
					processorManager.getExgProcessor().receiveExgMsgReturnStartup(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_RETURN_END:
					log.info("【接收】结束车辆定位信息交换请求");
					processorManager.getExgProcessor().receiveExgMsgReturnEnd(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK:
					log.info("【接收】申请交换指定车辆定位信息应答");
					processorManager.getExgProcessor().receiveExgMsgApplyForMonitorStartupAck(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK:
					log.info("【接收】取消申请交换指定车辆定位信息应答");
					processorManager.getExgProcessor().receiveExgMsgApplyForMonitorEndAck(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK:
					log.info("【接收】补发车辆定位信息应答");
					processorManager.getExgProcessor().receiveExgMsgApplyHisgnssDataAck(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_REPORT_DRIVER_INFO:
					log.info("【接收】上报驾驶员身份识别信息请求");
					processorManager.getExgProcessor().receiveExgMsgReportDriverInfo(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_TAKE_EWAYBILL_REQ:
					log.info("【接收】上报车辆电子运单请求");
					processorManager.getExgProcessor().receiveExgMsgTakeEwaybillReq(byteBuf, ctx.channel());
					break;
				default:
					log.info("未知消息类型:{}", Integer.toHexString(dataType));
					break;
				}
			}

			/**
			 * 平台间信息交互业务
			 */
			case MsgConstant.PrimaryServiceType.DOWN_PLATFORM_MSG: {
				BasePlatFormMsgEntity msgEntity = new BasePlatFormMsgEntity().decode(byteBuf);
				int dataType = msgEntity.getDataType();
				switch (dataType) {
				case MsgConstant.SubServiceType.DOWN_PLATFORM_MSG_POST_QUERY_REQ:
					log.info("【接收】平台查岗请求");
					processorManager.getPlatFormProcessor().receivePostQueryReq(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_EXG_MSG_HISTORY_ARCOSSAREA:
					log.info("【接收】下发平台间报文请求");
					processorManager.getPlatFormProcessor().receiveInfoReq(byteBuf, ctx.channel());
					break;
				default:
					log.info("未知消息类型:{}", Integer.toHexString(dataType));
					break;
				}
			}

			/**
			 * 车辆报警信息业务
			 */
			case MsgConstant.PrimaryServiceType.DOWN_WARN_MSG: {
				BaseWarnMsgEntity msgEntity = new BaseWarnMsgEntity().decode(byteBuf);
				int dataType = msgEntity.getDataType();
				switch (dataType) {
				case MsgConstant.SubServiceType.DOWN_WARN_MSG_URGE_TODO_REQ:
					log.info("【接收】报警督办请求");
					processorManager.getWarnProcessor().receiveUrgeTodoReq(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_WARN_MSG_INFORM_TIPS:
					log.info("【接收】报警预警");
					processorManager.getWarnProcessor().receiveInformTips(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_WARN_MSG_EXG_INFORM:
					log.info("【接收】实时交换报警信息");
					processorManager.getWarnProcessor().receiveExgInform(byteBuf, ctx.channel());
					break;
				default:
					log.info("未知消息类型:{}", Integer.toHexString(dataType));
					break;
				}
			}

			/**
			 * 车辆监管业务
			 */
			case MsgConstant.PrimaryServiceType.DOWN_CTRL_MSG: {
				BaseCtrlMsgEntity msgEntity = new BaseCtrlMsgEntity().decode(byteBuf);
				int dataType = msgEntity.getDataType();
				switch (dataType) {
				case MsgConstant.SubServiceType.DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ:
					log.info("【接收】车辆单向监听请求");
					processorManager.getCtrlProcessor().receiveMonitorVehicleReq(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_CTRL_MSG_TAKE_PHOTO_REQ:
					log.info("【接收】车辆拍照请求");
					processorManager.getCtrlProcessor().receiveTakePhotoReq(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_CTRL_MSG_TEXT_INFO_REQ:
					log.info("【接收】下发车辆报文请求");
					processorManager.getCtrlProcessor().receiveTextInfoReq(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_CTRL_MSG_TAKE_TRAVEL_REQ:
					log.info("【接收】上报车辆行驶记录请求");
					processorManager.getCtrlProcessor().receiveTakeTravelReq(byteBuf, ctx.channel());
					break;
				case MsgConstant.SubServiceType.DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ:
					log.info("【接收】车辆应急接入监管平台请求");
					processorManager.getCtrlProcessor().receiveEmergencyMonitoringReq(byteBuf, ctx.channel());
					break;
				default:
					log.info("未知消息类型:{}", dataType);
					break;
				}
			}

			/**
			 * 车辆静态信息交换业务
			 */
			case MsgConstant.PrimaryServiceType.DOWN_BASE_MSG: {
				BaseMsgEntity msgEntity = new BaseMsgEntity().decode(byteBuf);
				int dataType = msgEntity.getDataType();
				switch (dataType) {
				case MsgConstant.SubServiceType.DOWN_BASE_MSG_VEHICLE_ADDED:
					log.info("【接收】补报车辆静态信息请求");
					processorManager.getBaseMsgProcessor().receiveVehicleAdded(byteBuf, ctx.channel());
					break;
				default:
					log.info("未知消息类型:{}", dataType);
					break;
				}
			}

			default:
				log.info("未知消息:{}" + BufUtil.buf2HexStr(byteBuf));
				break;
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

}

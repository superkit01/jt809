package com.superkit.jt809.handler;

import com.superkit.jt809.manager.ProcessorManager;


public class RabbitmqHandler {

	private ProcessorManager processorManager;

	public RabbitmqHandler(ProcessorManager processorManager) {
		this.processorManager = processorManager;
	}

	public void handleTakeEwaybillMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getExgProcessor().sendExgMsgTakeEwaybillAck(entity.getVehicleNo(), entity.getVehicleColor(),
				"");

	}

	public void handleReportDriverInfoMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getExgProcessor().sendExgMsgReportDriverInfoAck(entity.getVehicleNo(),
				entity.getVehicleColor(), "", "", "", "");

	}

	/**
	 * 单向监听响应
	 */
	public void handleMonitorVehicleMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getCtrlProcessor().sendMonitorVehicleAck(entity.getVehicleNo(), entity.getVehicleColor(),
				(byte) 0);
	}

	/**
	 * 拍照响应
	 */
	public void handleTakePhotoMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getCtrlProcessor().sendTakePhotoAck(entity.getVehicleNo(), entity.getVehicleColor(), (byte) 0,
				new byte[0], (byte) 0, 0, (byte) 0, (byte) 0, new byte[0]);
	}

	/**
	 * 下发车辆报文响应
	 */
	public void handleTextInfoMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getCtrlProcessor().sendTextInfoAck(entity.getVehicleNo(), entity.getVehicleColor(), 0,
				(byte) 0);
	}

	/**
	 * 上报车辆行驶记录响应
	 */
	public void handleTakeTravelMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getCtrlProcessor().sendTakeTravelAck(entity.getVehicleNo(), entity.getVehicleColor(), "");
	}

	/**
	 * 上报车辆应急接入监管平台应答
	 */
	public void handleEmergencyMonitoringMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getCtrlProcessor().sendEmergencyMonitoringAck(entity.getVehicleNo(), entity.getVehicleColor(),
				(byte) 0);
	}

	/**
	 * 平台查岗应答
	 */
	public void handlePostQueryMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getPlatFormProcessor().sendPostQueryAck(0, "");
	}

	/**
	 * 报警督办应答
	 */
	public void handleUrgeTodoMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getWarnProcessor().sendUrgeTodoAck(entity.getVehicleNo(), entity.getVehicleColor(), 0,
				(byte) 0);
	}

	/**
	 * 申请交换指定车辆定位信息
	 */
	public void handleApplyForMonitorStartupMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getExgProcessor().sendExgMsgApplyForMonitorStartup(entity.getVehicleNo(),
				entity.getVehicleColor(), 0, 0);
	}

	/**
	 * 取消交换指定车辆定位信息
	 */
	public void handleApplyForMonitorEndMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getExgProcessor().sendExgMsgApplyForMonitorEnd(entity.getVehicleNo(),
				entity.getVehicleColor());
	}

	/**
	 * 补发车辆定位
	 */
	public void handleApplyHisgnssDataMessage(RabbitmqDto<?> entity) {
		// TODO 解析数据

		processorManager.getExgProcessor().sendExgMsgApplyHisgnssDataReq(entity.getVehicleNo(),
				entity.getVehicleColor(), 0, 0);
	}
}

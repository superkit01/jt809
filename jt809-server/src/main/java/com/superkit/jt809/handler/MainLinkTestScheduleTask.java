package com.superkit.jt809.handler;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.superkit.jt809.enums.LinkTypeEnum;
import com.superkit.jt809.manager.ChannelManager;
import com.superkit.jt809.manager.ProcessorManager;

/**
 * 主链路心跳
 */
@Component
public class MainLinkTestScheduleTask {

	@Autowired
	private ProcessorManager processorManager;

	/**
	 * 每分钟发送一次心跳包
	 */
	@Scheduled(fixedRate = 1000 * 60)
	public void run() {
		if (Objects.nonNull(ChannelManager.getChannel(LinkTypeEnum.MAIN_LINKS))) {
			processorManager.getLinkManagerProcessor().sendMainLinkTestReq();
		}
	}
}

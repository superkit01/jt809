package com.superkit.jt809.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.superkit.jt809.handler.RabbitmqHandler;
import com.superkit.jt809.manager.ProcessorManager;
import com.superkit.jt809.processor.BaseMsgProcessor;
import com.superkit.jt809.processor.CtrlProcessor;
import com.superkit.jt809.processor.ExgProcessor;
import com.superkit.jt809.processor.InfoStaticProcessor;
import com.superkit.jt809.processor.LinkManagerProcessor;
import com.superkit.jt809.processor.PlatFormProcessor;
import com.superkit.jt809.processor.WarnProcessor;

@Configuration
public class ServiceConfig {

	@Autowired
	public RabbitTemplate template;

	@Bean
	public BaseMsgProcessor baseMsgProcessor() {
		return new BaseMsgProcessor(template);
	}

	@Bean
	public CtrlProcessor ctrlProcessor() {
		return new CtrlProcessor(template);
	}

	@Bean
	public ExgProcessor exgProcessor() {
		return new ExgProcessor(template);
	}

	@Bean
	public InfoStaticProcessor infoStaticProcessor() {
		return new InfoStaticProcessor(template);
	}

	@Bean
	public LinkManagerProcessor linkManagerProcessor() {
		return new LinkManagerProcessor(template);
	}

	@Bean
	public PlatFormProcessor platFormProcessor() {
		return new PlatFormProcessor(template);
	}

	@Bean
	public WarnProcessor warnProcessor() {
		return new WarnProcessor(template);
	}

	@Bean
	public ProcessorManager processorManager(BaseMsgProcessor baseMsgProcessor, CtrlProcessor ctrlProcessor,
			ExgProcessor exgProcessor, InfoStaticProcessor infoStaticProcessor,
			LinkManagerProcessor linkManagerProcessor, PlatFormProcessor platFormProcessor,
			WarnProcessor warnProcessor) {

		ProcessorManager manager = new ProcessorManager();

		manager.setBaseMsgProcessor(baseMsgProcessor);
		manager.setCtrlProcessor(ctrlProcessor);
		manager.setExgProcessor(exgProcessor);
		manager.setInfoStaticProcessor(infoStaticProcessor);
		manager.setLinkManagerProcessor(linkManagerProcessor);
		manager.setPlatFormProcessor(platFormProcessor);
		manager.setWarnProcessor(warnProcessor());

		return manager;
	}

	@Bean
	public RabbitmqHandler handler(ProcessorManager processorManager) {
		return new RabbitmqHandler(processorManager);
	}

}

package com.superkit.jt809.manager;

import com.superkit.jt809.processor.BaseMsgProcessor;
import com.superkit.jt809.processor.CtrlProcessor;
import com.superkit.jt809.processor.ExgProcessor;
import com.superkit.jt809.processor.InfoStaticProcessor;
import com.superkit.jt809.processor.LinkManagerProcessor;
import com.superkit.jt809.processor.PlatFormProcessor;
import com.superkit.jt809.processor.WarnProcessor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessorManager {

	private  BaseMsgProcessor baseMsgProcessor;
	private  CtrlProcessor ctrlProcessor;
	private  ExgProcessor exgProcessor;
	private  InfoStaticProcessor infoStaticProcessor;
	private  LinkManagerProcessor linkManagerProcessor;
	private  PlatFormProcessor platFormProcessor;
	private  WarnProcessor warnProcessor;
	

}

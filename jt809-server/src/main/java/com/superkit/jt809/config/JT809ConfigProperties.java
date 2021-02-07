package com.superkit.jt809.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.superkit.jt809.JT809Config;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jt809")
@Data
public class JT809ConfigProperties  implements InitializingBean{

	private String serverIp;

	private Integer serverPort;
	
	private String localIp;

	private Integer localPort;
	
	private Long userId;

	private String password;

	
	private Long msgGnssscenterId;

	private String versionFlag;


	private Integer m1;

	private Integer ia1;

	private Integer ic1;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		JT809Config.init(this.getUserId(), this.getPassword(),this.getLocalIp(),
				this.getLocalPort(), this.getMsgGnssscenterId(), this.getVersionFlag(),
				this.getM1(), this.getIa1(), this.getIc1());
		
	}

}

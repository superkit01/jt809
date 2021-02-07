
package com.superkit.jt809;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Jt809ServerApplication {
	public static void main(String[] args) {

		SpringApplication.run(Jt809ServerApplication.class, args);

	}
}

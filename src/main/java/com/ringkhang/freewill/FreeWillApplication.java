package com.ringkhang.freewill;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class FreeWillApplication {
	public static void main(String[] args) {
		SpringApplication.run(FreeWillApplication.class, Arrays.toString(args));
	}
}
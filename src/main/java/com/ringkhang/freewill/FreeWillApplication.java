package com.ringkhang.freewill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class FreeWillApplication {
	public static void main(String[] args) {
		SpringApplication.run(FreeWillApplication.class, Arrays.toString(args));
	}
}
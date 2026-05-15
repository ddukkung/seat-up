package com.seatup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SeatUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeatUpApplication.class, args);
	}

}

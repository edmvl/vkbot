package ru.zhendozzz.vkbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VkbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(VkbotApplication.class, args);
	}

}

package com.xuelongjiang.websocketdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
public class WebsocketdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketdemoApplication.class, args);
	}


	/**
	 * 使用 websockt注解的时候，使用@EnableScheduling注解
	 * 启动的时候一直报错，增加这个bean 则报错解决。
	 * 报错信息：  Unexpected use of scheduler.
	 *https://stackoverflow.com/questions/49343692/websocketconfigurer-and-scheduled-are-not-work-well-in-an-application
	 *
	 * @return
	 */
	@Bean
	public TaskScheduler taskScheduler(){

		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.initialize();
		return taskScheduler;

	}


}

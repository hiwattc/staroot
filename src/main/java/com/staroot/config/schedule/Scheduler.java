package com.staroot.config.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Scheduler {

	// 매 1분마다 
	@Scheduled(cron = "0 1-59 1-23 * * *")
	public void aJob() {
		// 실행될 로직
		System.out.println("AAAAAAAAAAAAAAAA");
	}

	// 매월 1일 0시 0분 0초에 실행한다.
	@Scheduled(cron = "0 0 0 1 * *")
	public void anotherJob() {
		// 실행될 로직
		System.out.println("BBBBBBBBBBBBBBBBBBB");
		
	}

	@Scheduled(cron = "0/10 * * * * ?")
	public void anotherJob2() {
		//log.debug(new Date().toString());
		System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
	}
}

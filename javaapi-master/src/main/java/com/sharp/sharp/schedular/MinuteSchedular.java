package com.sharp.sharp.schedular;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sharp.sharp.entity.ContestJoined;
import com.sharp.sharp.service.HomeDashBoardService;

@Component
//@EnableScheduling
public class MinuteSchedular {
	@Autowired
	private HomeDashBoardService dashBoardService;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	//@Scheduled(cron = "0 * * ? * *")
	public void reportCurrentTime() {
		System.out.println("Current time = " + dateFormat.format(new Date()));
	//	List<ContestJoined> afterThreeMiutes = dashBoardService.getAfterThreeMinutes("32c39faa-3506-40e8-a594-12172d4893e7");
		//System.out.println(afterThreeMiutes);
	}

}

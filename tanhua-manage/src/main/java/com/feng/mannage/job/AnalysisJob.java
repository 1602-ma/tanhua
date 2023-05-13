package com.feng.mannage.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author f
 * @date 2023/5/13 10:28
 */
@Component
public class AnalysisJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void analysis() {
        System.out.println("now: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}

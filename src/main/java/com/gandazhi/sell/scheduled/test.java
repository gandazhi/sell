package com.gandazhi.sell.scheduled;

import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Log
public class test {

    @Scheduled(cron="0 0/1 8-20 * * ?")
    public void demo(){
        Thread current = Thread.currentThread();
        System.out.println("定时任务2:"+current.getId());
        log.info("ScheduledTest.executeUploadTask 定时任务2:"+current.getId() + ",name:"+current.getName());
    }
}

package com.sven.scheduler.service;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    public void demo(JobExecutionContext context) {
        System.out.println("test job started");
    }
}

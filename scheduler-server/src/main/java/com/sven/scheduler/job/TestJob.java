package com.sven.scheduler.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sven.scheduler.service.BusinessService;

@DisallowConcurrentExecution
public class TestJob extends QuartzJobBean {

    @Autowired
    private BusinessService businessService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        businessService.demo(context);
    }
}

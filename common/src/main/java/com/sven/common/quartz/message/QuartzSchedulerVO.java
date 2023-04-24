package com.sven.common.quartz.message;

import java.util.TimeZone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzSchedulerVO {
    private String jobName;
    private String jobGroup;
    private String jobClassPath;
    private String jobCron;
    private String jobTriggerName;
    private String jobTriggerGroup;
    private String jobStatus;
    private TimeZone jobTimeZone;
}

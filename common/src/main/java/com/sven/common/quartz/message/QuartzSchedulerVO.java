package com.sven.common.quartz.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartzSchedulerVO {
    private String jobName;
    private String jobGroup;
    private String jobCron;
    private String jobStatus;
}

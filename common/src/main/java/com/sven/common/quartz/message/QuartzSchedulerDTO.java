package com.sven.common.quartz.message;

import com.sven.common.domain.message.PageMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuartzSchedulerDTO extends PageMessage {
    private String jobClassPath;
    
    private String jobName;
    
    private String jobGroup;
    
    private String jobTriggerName;
    
    private String jobTriggerGroup;
    
    private String jobCron;
}

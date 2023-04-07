package com.sven.scheduler.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.domain.message.SystemEvent;
import com.sven.common.quartz.message.QuartzSchedulerDTO;
import com.sven.common.quartz.message.QuartzSchedulerVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    public ResponseMessage<IPage<QuartzSchedulerVO>> list(QuartzSchedulerDTO rq) {
        IPage<QuartzSchedulerVO> page = Page.of(rq.getPageNo(), rq.getPageSize());
        List<QuartzSchedulerVO> result = new ArrayList<>();
        
        ResponseMessage<IPage<QuartzSchedulerVO>> response = new ResponseMessage<>(page, 200);
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {
                GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(groupName);
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                    JobKey jobKey = trigger.getJobKey();
                    JobDetailImpl jobDetail = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                    
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                    
                    QuartzSchedulerVO vo = new QuartzSchedulerVO();
                    vo.setJobName(jobDetail.getName());
                    vo.setJobGroup(groupName);
                    vo.setJobCron(trigger.getCronExpression());
                    vo.setJobStatus(triggerState.toString());
                    
                    result.add(vo);
                }
            }
            page.setRecords(result);
        } catch (Exception ex) {
            log.error("查询定时任务列表失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_jobList_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_jobList_exception_event));
        }
        
        return response;
    }
    
    @SuppressWarnings("unchecked")
    public ResponseMessage<String> addjob(QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = new ResponseMessage<>("创建定时任务成功", 200);
        
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(rq.getJobClassPath());
            
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                                            .withIdentity(rq.getJobName(), rq.getJobGroup())
                                            .build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                                                .withIdentity(rq.getTriggerName(), rq.getTriggerGroup())
                                                .startNow()
                                                .withSchedule(CronScheduleBuilder.cronSchedule(rq.getJobCron()))
                                                .build();
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException ex) {
            log.error("创建定时任务失败[ClassNotFoundException]" + ex);
            response.setCode(SystemEvent.quartz_addjob_classnotfoundexception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_addjob_classnotfoundexception_event));
        } catch (SchedulerException ex) {
            log.error("创建定时任务失败[SchedulerException]" + ex);
            response.setCode(SystemEvent.quartz_addjob_schedulerexception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_addjob_schedulerexception_event));
        } catch (Exception ex) {
            log.error("创建定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_addjob_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_addjob_exception_event));
        }
        
        return response;
    }
    
    public ResponseMessage<String> pauseJob(QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = new ResponseMessage<>("暂停定时任务成功", 200);
        try {
            scheduler.pauseJob(JobKey.jobKey(rq.getJobName(), rq.getJobGroup()));
        } catch (Exception ex) {
            log.error("暂停定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_pauseJob_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_pauseJob_exception_event));
        }

        return response;
    }

    public ResponseMessage<String> resumeJob(QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = new ResponseMessage<>("恢复定时任务成功", 200);
        try {
            scheduler.resumeJob(JobKey.jobKey(rq.getJobName(), rq.getJobGroup()));
        } catch (Exception ex) {
            log.error("恢复定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_resumeJob_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_resumeJob_exception_event));
        }

        return response;
    }

    public ResponseMessage<String> rescheduleJob(QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = new ResponseMessage<>("更新定时任务成功", 200);
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(rq.getJobName(), rq.getJobGroup());
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(rq.getJobCron());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception ex) {
            log.error("更新定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_rescheduleJob_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_rescheduleJob_exception_event));
        }

        return response;
    }

    public ResponseMessage<String> deleteJob(QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = new ResponseMessage<>("删除定时任务成功", 200);
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(rq.getJobName(), rq.getJobGroup()));
            scheduler.unscheduleJob(TriggerKey.triggerKey(rq.getJobName(), rq.getJobGroup()));
            scheduler.deleteJob(JobKey.jobKey(rq.getJobName(), rq.getJobGroup()));
        } catch (Exception ex) {
            log.error("删除定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_deleteJob_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_deleteJob_exception_event));
        }

        return response;
    }
    
    public ResponseMessage<String> runOnce(String jobName, String groupName) {
        ResponseMessage<String> response = new ResponseMessage<>("运行定时任务成功", 200);
        try {
            scheduler.triggerJob(JobKey.jobKey(jobName, groupName));
        } catch (Exception ex) {
            log.error("运行定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_runOnce_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_runOnce_exception_event));
        }

        return response;
    }
    
    public ResponseMessage<String> startAllJobs() {
        ResponseMessage<String> response = new ResponseMessage<>("运行所有定时任务成功", 200);
        try {
            scheduler.start();
        } catch (Exception ex) {
            log.error("运行所有定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_startAllJobs_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_startAllJobs_exception_event));
        }

        return response;
    }

    public ResponseMessage<String> pauseAllJobs() {
        ResponseMessage<String> response = new ResponseMessage<>("暂停所有定时任务成功", 200);
        try {
            scheduler.pauseAll();
        } catch (Exception ex) {
            log.error("暂停所有定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_pauseAllJobs_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_pauseAllJobs_exception_event));
        }

        return response;
    }

    public ResponseMessage<String> resumeAllJobs() {
        ResponseMessage<String> response = new ResponseMessage<>("恢复所有定时任务成功", 200);
        try {
            scheduler.resumeAll();
        } catch (Exception ex) {
            log.error("恢复所有定时任务失败[Exception]" + ex);
            response.setCode(SystemEvent.quartz_resumeAllJobs_exception_event.getErrorCode());
            response.setError(new ErrorDetails(SystemEvent.quartz_resumeAllJobs_exception_event));
        }

        return response;
    }
}

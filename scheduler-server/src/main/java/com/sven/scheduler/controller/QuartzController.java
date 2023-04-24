package com.sven.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.quartz.message.QuartzSchedulerDTO;
import com.sven.common.quartz.message.QuartzSchedulerVO;
import com.sven.scheduler.service.QuartzService;

@RestController
@RequestMapping(path = "/quartz")
public class QuartzController {
    @Autowired
    private QuartzService quartzService;
    
    @PostMapping("/list")
    public IBaseResponseMessage<?> listAll(@RequestBody QuartzSchedulerDTO rq) {
        ResponseMessage<IPage<QuartzSchedulerVO>> response = quartzService.list(rq);
        
        return response;
    }
    
    @PostMapping("/addJob")
    public IBaseResponseMessage<?> addJob(@RequestBody QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = quartzService.addjob(rq);

        return response;
    }
    
    @PostMapping("/pauseJob")
    public IBaseResponseMessage<?> pauseJob(@RequestBody QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = quartzService.pauseJob(rq);
        
        return response;
    }
    
    @PostMapping("/resumeJob")
    public IBaseResponseMessage<?> resumeJob(@RequestBody QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = quartzService.resumeJob(rq);
        
        return response;
    }
    
    @PutMapping("/rescheduleJob")
    public IBaseResponseMessage<?> rescheduleJob(@RequestBody QuartzSchedulerDTO rq) {
        ResponseMessage<String> response = quartzService.rescheduleJob(rq);

        return response;
    }

    @PutMapping("/startAllJobs")
    public IBaseResponseMessage<?> startAllJobs() {
        ResponseMessage<String> response = quartzService.startAllJobs();

        return response;
    }

    @PutMapping("/pauseAllJobs")
    public IBaseResponseMessage<?> pauseAllJobs() {
        ResponseMessage<String> response = quartzService.pauseAllJobs();

        return response;
    }

    @PutMapping("/resumeAllJobs")
    public IBaseResponseMessage<?> resumeAllJobs() {
        ResponseMessage<String> response = quartzService.resumeAllJobs();

        return response;
    }
    
    @PostMapping("/deleteJob")
    public IBaseResponseMessage<?> deleteJob(@RequestBody QuartzSchedulerDTO req) {
        ResponseMessage<String> response = quartzService.deleteJob(req);
        
        return response;
    }
}

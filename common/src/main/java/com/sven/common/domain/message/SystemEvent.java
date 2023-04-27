package com.sven.common.domain.message;

public enum SystemEvent {

    sys_default_event(10000, "default event"),
    valid_exception_event(10001, "校验失败"),
    
    quartz_addjob_exception_event(20000, "创建定时任务失败[SchedulerException]"),
    quartz_addjob_classnotfoundexception_event(20001, "创建定时任务失败[ClassNotFoundException]"),
    quartz_addjob_schedulerexception_event(20002, "创建定时任务失败[SchedulerException]"),
    quartz_pauseJob_exception_event(20003, "暂停定时任务失败"),
    quartz_resumeJob_exception_event(20004, "恢复定时任务失败"),
    quartz_rescheduleJob_exception_event(20005, "更新定时任务失败"),
    quartz_deleteJob_exception_event(20006, "删除定时任务失败"),
    quartz_startAllJobs_exception_event(20007, "运行所有定时任务失败"),
    quartz_pauseAllJobs_exception_event(20008, "暂停所有定时任务失败"),
    quartz_resumeAllJobs_exception_event(20009, "恢复所有定时任务失败"),
    quartz_runOnce_exception_event(20010, "运行一次定时任务失败"),
    quartz_jobList_exception_event(20011, "查询定时任务列表失败");

    SystemEvent(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private int errorCode;
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}

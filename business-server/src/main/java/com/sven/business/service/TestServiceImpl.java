package com.sven.business.service;

import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sven.common.dubbo.server.IUserService;
import com.sven.common.exception.BusinessException;
import com.sven.common.exception.BusinessExceptionEnum;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestServiceImpl {

    @Autowired
    private IUserService userService;

    @GlobalTransactional(timeoutMills = 300000, name = "seata-test")
    public void seataTestCommit() {
        log.info("purchase begin ... xid: " + RootContext.getXID());
        userService.addUser("seata");
    }

    @GlobalTransactional(timeoutMills = 300000, name = "seata-test")
    public void seataTestRollback() {
        log.info("purchase begin ... xid: " + RootContext.getXID());
        userService.addUser("seata");
        throw new BusinessException(BusinessExceptionEnum.user_not_found, null, null);
    }
}

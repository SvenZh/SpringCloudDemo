package com.sven.common.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.vo.UserInfoVO;

@FeignClient(name = "system-server")
public interface SystemServerFeignClient {

    @GetMapping("/user/retrieveUserInfoByName")
    public ResponseMessage<UserInfoVO> retrieveUserInfoByName(@RequestParam("userName") String userName);
}

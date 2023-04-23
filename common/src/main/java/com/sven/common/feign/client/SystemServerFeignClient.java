package com.sven.common.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.sven.common.domain.message.ResponseMessage;

@FeignClient(name = "system-server")
public interface SystemServerFeignClient {

    @GetMapping("/ping")
    public ResponseMessage<String> ping();
}

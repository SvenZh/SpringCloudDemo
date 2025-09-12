package com.sven.common.feign.client;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.security.NoToken;
import com.sven.common.vo.PerimissionVO;
import com.sven.common.vo.RoleVO;
import com.sven.common.vo.UserVO;

@FeignClient(name = "system-server")
public interface SystemServerFeignClient {

    @NoToken
    @GetMapping("/internal/retrieveUserInfoByName")
    public ResponseMessage<UserVO> retrieveUserInfoByName(@RequestParam("userName") String userName);
    
    @NoToken
    @GetMapping("/internal/retrieveUserInfoByphone")
    public ResponseMessage<UserVO> retrieveUserInfoByPhone(@RequestParam("phone") String phone);

    @GetMapping("/role/retrieveRoleInfoByRoleName")
    public ResponseMessage<RoleVO> retrieveRoleInfoByRoleName(@RequestParam("roleName") String roleName);

    @GetMapping("/rolePerimission/retrieveRolePerimissionInfoByRoleId")
    public ResponseMessage<List<PerimissionVO>> retrieveRolePerimissionInfoByRoleId(
            @RequestParam("roleId") Long roleId);

    @GetMapping("/rolePerimission/hasPerimission")
    public ResponseMessage<Boolean> hasPerimission(@RequestParam("authority") Set<String> authority,
            @RequestParam("requestPath") String requestPath);

}

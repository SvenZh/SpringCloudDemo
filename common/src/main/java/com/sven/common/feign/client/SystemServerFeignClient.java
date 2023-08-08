package com.sven.common.feign.client;

import java.util.List;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.vo.PerimissionInfoVO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.common.vo.UserInfoVO;

@FeignClient(name = "system-server")
public interface SystemServerFeignClient {

    @GetMapping("/user/retrieveUserInfoByName")
    public ResponseMessage<UserInfoVO> retrieveUserInfoByName(@RequestParam("userName") String userName);

    @GetMapping("/role/retrieveRoleInfoByRoleName")
    public ResponseMessage<RoleInfoVO> retrieveRoleInfoByRoleName(@RequestParam("roleName") String roleName);

    @GetMapping("/rolePerimission/retrieveRolePerimissionInfoByRoleId")
    public ResponseMessage<List<PerimissionInfoVO>> retrieveRolePerimissionInfoByRoleId(
            @RequestParam("roleId") Long roleId);

    @GetMapping("/rolePerimission/hasPerimission")
    public ResponseMessage<Boolean> hasPerimission(@RequestParam("authority") Set<String> authority,
            @RequestParam("requestPath") String requestPath);

}

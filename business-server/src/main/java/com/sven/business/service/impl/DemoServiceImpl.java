package com.sven.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.business.dto.DemoDTO;
import com.sven.business.dto.UserInfoDTO;
import com.sven.business.entity.UserInfoEntity;
import com.sven.business.feign.client.SystemServerFeignClient;
import com.sven.business.mapper.DemoServiceMapper;
import com.sven.business.service.IDemoService;
import com.sven.business.vo.UserInfoVO;
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.domain.message.SystemEvent;

@Service
@DS("master")
public class DemoServiceImpl extends ServiceImpl<DemoServiceMapper, UserInfoEntity> implements IDemoService {
    @Autowired
    private SystemServerFeignClient systemServerFeignClient;

    @Override
    public ResponseMessage<IPage<UserInfoVO>> getUserInfoList(DemoDTO body) {

        List<UserInfoEntity> result = this.getBaseMapper().selectList(new QueryWrapper<>());

        List<UserInfoVO> response = result.stream().map(entity -> {
            UserInfoVO vo = new UserInfoVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());

        Page<UserInfoVO> page = Page.of(body.getPageNo(), body.getPageSize(), response.size());
        page.setRecords(response);

        return new ResponseMessage<>(page, 200);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseMessage<Boolean> insertUser(List<UserInfoDTO> body) {

        List<UserInfoEntity> userInfoEntities = body.stream().map(dto -> {
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            BeanUtils.copyProperties(dto, userInfoEntity);
            
            return userInfoEntity;
        }).collect(Collectors.toList());

        boolean response = this.saveBatch(userInfoEntities, DEFAULT_BATCH_SIZE);

        return new ResponseMessage<Boolean>(response, 200);
    }
    
    @Override
    public ResponseMessage<String> getResponseFromAnotherSystem() {

        ResponseMessage<String> result = systemServerFeignClient.ping();
        if (!result.isSuccess()) {
            return new ResponseMessage<>(new ErrorDetails(SystemEvent.sys_default_event),
                    SystemEvent.sys_default_event.getErrorCode());
        }
        return new ResponseMessage<>(result.getData(), 200);
    }
}

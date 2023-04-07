package com.sven.service.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.service.dto.DemoDTO;
import com.sven.service.entity.UserInfoEntity;
import com.sven.service.mapper.DemoServiceMapper;
import com.sven.service.service.IDemoService;
import com.sven.service.vo.UserInfoVO;

@Service
@DS("master")
public class DemoServiceImpl extends ServiceImpl<DemoServiceMapper, UserInfoEntity> implements IDemoService {

    @Override
    public IPage<UserInfoVO> getUserInfoList(DemoDTO body) {
        
        List<UserInfoEntity> result = this.getBaseMapper().selectList(new QueryWrapper<>());

        List<UserInfoVO> response = result.stream().map(entity -> {
            UserInfoVO vo = new UserInfoVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());
        
        Page<UserInfoVO> page = Page.of(body.getPageNo(), body.getPageSize(), response.size());
        page.setRecords(response);
        return page;
    }
}

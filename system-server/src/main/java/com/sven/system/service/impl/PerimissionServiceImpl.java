package com.sven.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.PerimissionInfoDTO;
import com.sven.common.vo.PerimissionInfoVO;
import com.sven.system.entity.PerimissionInfoEntity;
import com.sven.system.mapper.PerimissionServiceMapper;
import com.sven.system.service.IPerimissionService;

@Service
public class PerimissionServiceImpl extends ServiceImpl<PerimissionServiceMapper, PerimissionInfoEntity>
        implements IPerimissionService {

    @Override
    public ResponseMessage<List<PerimissionInfoVO>> retrievePerimissionList(PerimissionInfoDTO dto) {
        LambdaQueryWrapper<PerimissionInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), PerimissionInfoEntity::getName, dto.getName());

        List<PerimissionInfoEntity> perimissionInfoEntities = this.baseMapper.selectList(queryWrapper);

        List<PerimissionInfoVO> response = perimissionInfoEntities.stream().map(entity -> {
            PerimissionInfoVO vo = new PerimissionInfoVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<IPage<PerimissionInfoVO>> retrievePerimissionPage(PerimissionInfoDTO dto) {
        LambdaQueryWrapper<PerimissionInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<PerimissionInfoEntity> perimissionInfoEntities = this.baseMapper.selectList(queryWrapper);

        List<PerimissionInfoVO> response = perimissionInfoEntities.stream().map(entity -> {
            PerimissionInfoVO vo = new PerimissionInfoVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        Page<PerimissionInfoVO> page = Page.of(dto.getPageNo(), dto.getPageSize(), response.size());
        page.setRecords(response);

        return ResponseMessage.ok(page);
    }

    @Override
    public ResponseMessage<Boolean> createPerimission(PerimissionInfoDTO dto) {
        PerimissionInfoEntity entity = new PerimissionInfoEntity();
        entity.setCreateAt(new Date());
        entity.setCreateBy(1665943054155702273L);
        entity.setUpdateAt(new Date());
        entity.setUpdateBy(1665943054155702273L);
        BeanUtils.copyProperties(dto, entity);

        int result = this.baseMapper.insert(entity);

        return ResponseMessage.ok(result > 0);
    }

    @Override
    public ResponseMessage<PerimissionInfoVO> retrievePerimissionInfoById(Long perimissionId) {
        PerimissionInfoVO response = new PerimissionInfoVO();
        PerimissionInfoEntity entity = this.baseMapper.selectById(perimissionId);

        BeanUtils.copyProperties(entity, response);

        return ResponseMessage.ok(response);
    }
}

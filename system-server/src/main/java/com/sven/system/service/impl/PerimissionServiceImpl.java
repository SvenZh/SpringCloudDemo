package com.sven.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.PerimissionDTO;
import com.sven.common.vo.PerimissionVO;
import com.sven.system.dao.PerimissionServiceDAO;
import com.sven.system.entity.PerimissionEntity;
import com.sven.system.service.IPerimissionService;

@Service
public class PerimissionServiceImpl implements IPerimissionService {
    
    @Autowired
    private PerimissionServiceDAO perimissionServiceDAO;
    
    @Override
    public ResponseMessage<List<PerimissionVO>> retrievePerimissionList(final PerimissionDTO dto) {
        List<PerimissionEntity> perimissionInfoEntities = perimissionServiceDAO.selectList(dto);

        List<PerimissionVO> response = perimissionInfoEntities.stream().map(entity -> {
            PerimissionVO vo = new PerimissionVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<IPage<PerimissionVO>> retrievePerimissionPage(final PerimissionDTO dto) {
        List<PerimissionEntity> perimissionInfoEntities = perimissionServiceDAO.selectList(dto);

        List<PerimissionVO> response = perimissionInfoEntities.stream().map(entity -> {
            PerimissionVO vo = new PerimissionVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        Page<PerimissionVO> page = Page.of(dto.getPageNo(), dto.getPageSize(), response.size());
        page.setRecords(response);

        return ResponseMessage.ok(page);
    }

    @Override
    public ResponseMessage<Boolean> createPerimission(final PerimissionDTO dto) {
        PerimissionEntity entity = new PerimissionEntity();
        BeanUtils.copyProperties(dto, entity);

        int result = perimissionServiceDAO.insert(entity);
        
        return ResponseMessage.ok(result > 0);
    }

    @Override
    public ResponseMessage<PerimissionVO> retrievePerimissionInfoById(final Long perimissionId) {
        PerimissionVO response = new PerimissionVO();
        PerimissionEntity entity = perimissionServiceDAO.selectById(perimissionId);

        BeanUtils.copyProperties(entity, response);

        return ResponseMessage.ok(response);
    }
}

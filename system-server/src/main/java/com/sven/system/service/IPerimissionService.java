package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.PerimissionInfoDTO;
import com.sven.common.vo.PerimissionInfoVO;
import com.sven.system.entity.PerimissionInfoEntity;

public interface IPerimissionService extends IService<PerimissionInfoEntity> {

    ResponseMessage<List<PerimissionInfoVO>> retrievePerimissionList(PerimissionInfoDTO dto);

    ResponseMessage<IPage<PerimissionInfoVO>> retrievePerimissionPage(PerimissionInfoDTO dto);

    ResponseMessage<Boolean> createPerimission(PerimissionInfoDTO dto);
    
    ResponseMessage<PerimissionInfoVO> retrievePerimissionInfoById(Long perimissionId);

}

package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.PerimissionDTO;
import com.sven.common.vo.PerimissionVO;

public interface IPerimissionService {

    ResponseMessage<List<PerimissionVO>> retrievePerimissionList(final PerimissionDTO dto);

    ResponseMessage<IPage<PerimissionVO>> retrievePerimissionPage(final PerimissionDTO dto);

    ResponseMessage<Boolean> createPerimission(final PerimissionDTO dto);
    
    ResponseMessage<PerimissionVO> retrievePerimissionInfoById(final Long perimissionId);

}

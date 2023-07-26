package com.sven.system.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserInfoDTO;
import com.sven.common.feign.client.SystemServerFeignClient;
import com.sven.common.vo.UserInfoVO;
import com.sven.system.entity.UserInfoEntity;
import com.sven.system.mapper.UserServiceMapper;
import com.sven.system.service.impl.UserServiceImpl;

public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl mockUserServiceImpl;

    @Mock
    private SystemServerFeignClient mockSystemServerFeignClient;

    @Mock
    private UserServiceMapper mockUserServiceMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserInfoListHappyPath() {
        when(mockUserServiceMapper.selectList(Mockito.any())).thenReturn(mockSelectList());

        ResponseMessage<IPage<UserInfoVO>> response = mockUserServiceImpl.userPage(mockGetUserInfoListReqeust());
        List<UserInfoVO> records = response.getData().getRecords();
        
        assertEquals(1, records.size());
        assertEquals(200, response.getCode());
        assertEquals("address", records.get(0).getAddress());
        assertEquals("name", records.get(0).getName());
        assertEquals("email", records.get(0).getEmail());
        assertNull(response.getError());
    }

    private List<UserInfoEntity> mockSelectList() {
        List<UserInfoEntity> response = new ArrayList<UserInfoEntity>();
        UserInfoEntity entity = new UserInfoEntity();
        entity.setAddress("address");
        entity.setEmail("email");
        entity.setName("name");

        response.add(entity);

        return response;
    }

    private UserInfoDTO mockGetUserInfoListReqeust() {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setPageSize(10);
        dto.setPageNo(1);

        return dto;
    }

}

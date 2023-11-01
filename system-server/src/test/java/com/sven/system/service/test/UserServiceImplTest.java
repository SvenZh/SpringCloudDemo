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
import com.sven.common.dto.UserDTO;
import com.sven.common.feign.client.SystemServerFeignClient;
import com.sven.common.vo.UserVO;
import com.sven.system.entity.UserEntity;
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

        ResponseMessage<IPage<UserVO>> response = mockUserServiceImpl.retrieveUserPage(mockGetUserInfoListReqeust());
        List<UserVO> records = response.getData().getRecords();
        
        assertEquals(1, records.size());
        assertEquals(200, response.getCode());
        assertEquals("address", records.get(0).getAddress());
        assertEquals("name", records.get(0).getName());
        assertEquals("email", records.get(0).getEmail());
        assertNull(response.getError());
    }

    private List<UserEntity> mockSelectList() {
        List<UserEntity> response = new ArrayList<UserEntity>();
        UserEntity entity = new UserEntity();
        entity.setAddress("address");
        entity.setEmail("email");
        entity.setName("name");

        response.add(entity);

        return response;
    }

    private UserDTO mockGetUserInfoListReqeust() {
        UserDTO dto = new UserDTO();
        dto.setPageSize(10);
        dto.setPageNo(1);

        return dto;
    }

}

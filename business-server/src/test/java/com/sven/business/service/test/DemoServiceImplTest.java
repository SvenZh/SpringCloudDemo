package com.sven.business.service.test;

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
import com.sven.business.dto.DemoDTO;
import com.sven.business.entity.UserInfoEntity;
import com.sven.business.mapper.DemoServiceMapper;
import com.sven.business.service.impl.DemoServiceImpl;
import com.sven.business.vo.UserInfoVO;
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.feign.client.SystemServerFeignClient;

public class DemoServiceImplTest {
    @InjectMocks
    private DemoServiceImpl mockDemoServiceImpl;

    @Mock
    private SystemServerFeignClient mockSystemServerFeignClient;

    @Mock
    private DemoServiceMapper mockDemoServiceMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserInfoListHappyPath() {
        when(mockDemoServiceMapper.selectList(Mockito.any())).thenReturn(mockSelectList());

        ResponseMessage<IPage<UserInfoVO>> response = mockDemoServiceImpl.getUserInfoList(mockGetUserInfoListReqeust());
        List<UserInfoVO> records = response.getData().getRecords();
        
        assertEquals(1, records.size());
        assertEquals(200, response.getCode());
        assertEquals("address", records.get(0).getAddress());
        assertEquals("name", records.get(0).getName());
        assertEquals("email", records.get(0).getEmail());
        assertNull(response.getError());
    }

    @Test
    public void getResponseFromAnotherSystemHappyPath() {
        when(mockSystemServerFeignClient.ping()).thenReturn(new ResponseMessage<>("pong", 200));

        ResponseMessage<String> response = mockDemoServiceImpl.getResponseFromAnotherSystem();
        
        assertEquals(200, response.getCode());
        assertEquals("pong", response.getData());
        assertNull(response.getError());
    }

    @Test
    public void getResponseFromAnotherSystemUnHappyPath() {
        when(mockSystemServerFeignClient.ping())
                .thenReturn(new ResponseMessage<>(new ErrorDetails(500, "error")));

        ResponseMessage<String> response = mockDemoServiceImpl.getResponseFromAnotherSystem();
        
        assertEquals(10000, response.getCode());
        assertEquals(10000, response.getError().getErrorCode());
        assertEquals("default event", response.getError().getErrorMessage());
        assertNull(response.getData());
    }

    @Test(expected = RuntimeException.class)
    public void getResponseFromAnotherSystemException() {
        when(mockSystemServerFeignClient.ping()).thenThrow(new RuntimeException("excpetion"));

        ResponseMessage<String> response = mockDemoServiceImpl.getResponseFromAnotherSystem();
        
        assertEquals(500, response.getCode());
        assertEquals(500, response.getError().getErrorCode());
        assertEquals("excpetion", response.getError().getErrorMessage());
        assertNull(response.getData());
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

    private DemoDTO mockGetUserInfoListReqeust() {
        DemoDTO dto = new DemoDTO();
        dto.setName("test");
        dto.setPageSize(10);
        dto.setPageNo(1);

        return dto;
    }

}

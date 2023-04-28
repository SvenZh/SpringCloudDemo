package com.sven.business.controller.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sven.business.controller.DemoController;
import com.sven.business.dto.DemoDTO;
import com.sven.business.service.IDemoService;
import com.sven.business.vo.UserInfoVO;
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;

public class DemoControllerTest {
    @InjectMocks
    private DemoController mockDemoController;

    @Mock
    private IDemoService mockDemoService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void demoHappyPath() {

        Mockito.when(mockDemoService.getUserInfoList(Mockito.any(DemoDTO.class)))
                .thenReturn(mockDemoHappyPathResponse());

        ResponseMessage<IPage<UserInfoVO>> response = (ResponseMessage<IPage<UserInfoVO>>) mockDemoController
                .demo(mockDemoRequest());

        List<UserInfoVO> vo = response.getData().getRecords();

        Assert.assertEquals(1, vo.size());
        Assert.assertEquals("address", vo.get(0).getAddress());
        Assert.assertEquals("email", vo.get(0).getEmail());
        Assert.assertEquals("name", vo.get(0).getName());
        Assert.assertNull(response.getError());
    }

    @Test
    public void demoUnHappyPath() {

        Mockito.when(mockDemoService.getUserInfoList(Mockito.any(DemoDTO.class)))
                .thenReturn(mockDemoUnHappyPathResponse());

        ResponseMessage<IPage<UserInfoVO>> response = (ResponseMessage<IPage<UserInfoVO>>) mockDemoController
                .demo(mockDemoRequest());

        Assert.assertEquals(500, response.getError().getErrorCode());
        Assert.assertEquals("error 500", response.getError().getErrorMessage());
    }

    private DemoDTO mockDemoRequest() {
        DemoDTO dto = new DemoDTO();
        dto.setTestValid("testValid");

        return dto;
    }

    private ResponseMessage<IPage<UserInfoVO>> mockDemoHappyPathResponse() {
        List<UserInfoVO> vos = new ArrayList<>();
        UserInfoVO vo = new UserInfoVO();
        vo.setAddress("address");
        vo.setEmail("email");
        vo.setName("name");
        vos.add(vo);
        Page<UserInfoVO> page = Page.of(10, 1, vos.size());
        page.setRecords(vos);

        return new ResponseMessage<>(page, 200);
    }

    private ResponseMessage<IPage<UserInfoVO>> mockDemoUnHappyPathResponse() {
        ErrorDetails errors = new ErrorDetails(500, "error 500");

        return new ResponseMessage<>(errors);
    }
}

package com.sven.system.controller.test;

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
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserInfoDTO;
import com.sven.common.vo.UserInfoVO;
import com.sven.system.controller.UserController;
import com.sven.system.service.IUserService;

public class UserControllerTest {
    @InjectMocks
    private UserController mockUserController;

    @Mock
    private IUserService mockUserService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void demoHappyPath() {

        Mockito.when(mockUserService.retrieveUserPage(Mockito.any(UserInfoDTO.class)))
                .thenReturn(mockUserPageHappyPathResponse());

        ResponseMessage<IPage<UserInfoVO>> response = (ResponseMessage<IPage<UserInfoVO>>) mockUserController
                .retrieveUserPage(mockUserPageRequest());

        List<UserInfoVO> vo = response.getData().getRecords();

        Assert.assertEquals(1, vo.size());
        Assert.assertEquals("address", vo.get(0).getAddress());
        Assert.assertEquals("email", vo.get(0).getEmail());
        Assert.assertEquals("name", vo.get(0).getName());
        Assert.assertNull(response.getError());
    }

    @Test
    public void demoUnHappyPath() {

        Mockito.when(mockUserService.retrieveUserPage(Mockito.any(UserInfoDTO.class)))
                .thenReturn(mockUserPageUnHappyPathResponse());

        ResponseMessage<IPage<UserInfoVO>> response = (ResponseMessage<IPage<UserInfoVO>>) mockUserController
                .retrieveUserPage(mockUserPageRequest());

        Assert.assertEquals(500, response.getError().getErrorCode());
        Assert.assertEquals("error 500", response.getError().getErrorMessage());
    }

    private UserInfoDTO mockUserPageRequest() {
        UserInfoDTO dto = new UserInfoDTO();

        return dto;
    }

    private ResponseMessage<IPage<UserInfoVO>> mockUserPageHappyPathResponse() {
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

    private ResponseMessage<IPage<UserInfoVO>> mockUserPageUnHappyPathResponse() {
        ErrorDetails errors = new ErrorDetails(500, "error 500");

        return new ResponseMessage<>(errors);
    }
}

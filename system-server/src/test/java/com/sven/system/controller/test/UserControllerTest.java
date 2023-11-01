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
import com.sven.common.dto.UserDTO;
import com.sven.common.vo.UserVO;
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

        Mockito.when(mockUserService.retrieveUserPage(Mockito.any(UserDTO.class)))
                .thenReturn(mockUserPageHappyPathResponse());

        ResponseMessage<IPage<UserVO>> response = (ResponseMessage<IPage<UserVO>>) mockUserController
                .retrieveUserPage(mockUserPageRequest());

        List<UserVO> vo = response.getData().getRecords();

        Assert.assertEquals(1, vo.size());
        Assert.assertEquals("address", vo.get(0).getAddress());
        Assert.assertEquals("email", vo.get(0).getEmail());
        Assert.assertEquals("name", vo.get(0).getName());
        Assert.assertNull(response.getError());
    }

    @Test
    public void demoUnHappyPath() {

        Mockito.when(mockUserService.retrieveUserPage(Mockito.any(UserDTO.class)))
                .thenReturn(mockUserPageUnHappyPathResponse());

        ResponseMessage<IPage<UserVO>> response = (ResponseMessage<IPage<UserVO>>) mockUserController
                .retrieveUserPage(mockUserPageRequest());

        Assert.assertEquals(500, response.getError().getErrorCode());
        Assert.assertEquals("error 500", response.getError().getErrorMessage());
    }

    private UserDTO mockUserPageRequest() {
        UserDTO dto = new UserDTO();

        return dto;
    }

    private ResponseMessage<IPage<UserVO>> mockUserPageHappyPathResponse() {
        List<UserVO> vos = new ArrayList<>();
        UserVO vo = new UserVO();
        vo.setAddress("address");
        vo.setEmail("email");
        vo.setName("name");
        vos.add(vo);
        Page<UserVO> page = Page.of(10, 1, vos.size());
        page.setRecords(vos);

        return new ResponseMessage<>(page, 200);
    }

    private ResponseMessage<IPage<UserVO>> mockUserPageUnHappyPathResponse() {
        ErrorDetails errors = new ErrorDetails(500, "error 500");

        return new ResponseMessage<>(errors);
    }
}

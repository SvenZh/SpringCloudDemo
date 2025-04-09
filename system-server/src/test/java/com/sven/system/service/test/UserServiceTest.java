package com.sven.system.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserDTO;
import com.sven.common.vo.UserVO;
import com.sven.system.dao.UserServiceDAO;
import com.sven.system.entity.UserEntity;
import com.sven.system.service.impl.UserRoleServiceImpl;
import com.sven.system.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserServiceDAO userServiceDaoMock;
    
    @Mock
    private UserRoleServiceImpl UserRoleServiceImplMock;
    
    @InjectMocks
    private UserServiceImpl service;
    
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
    
    @Test
    public void testRetrieveUserListHappyPath() {
        List<UserEntity> mockUserEntities = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("testName");
        
        mockUserEntities.add(userEntity);
        
        UserDTO dto = new UserDTO();
        Mockito.when(userServiceDaoMock.selectList(dto)).thenReturn(mockUserEntities);
        Mockito.when(UserRoleServiceImplMock.retrieveUserRoleInfoByUserId(Mockito.anyLong())).thenReturn(ResponseMessage.ok(Lists.newArrayList()));
        
        ResponseMessage<List<UserVO>> result = service.retrieveUserList(dto);
        
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(1, result.getData().size());
        Assertions.assertEquals("testName", result.getData().get(0).getName());
    }
}

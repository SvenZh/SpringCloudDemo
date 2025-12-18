package com.sven.common.config;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sven.common.constant.UserContextHolder;
import com.sven.common.security.UserInfo;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        UserInfo userInfo = UserContextHolder.getUser();
        Long id = 0L;
        if (Objects.nonNull(userInfo)) {
            id = userInfo.getId();
        }

        this.strictInsertFill(metaObject, "createBy", Long.class, id);
        this.strictInsertFill(metaObject, "createAt", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateBy", Long.class, id);
        this.strictInsertFill(metaObject, "updateAt", LocalDateTime.class, LocalDateTime.now()); 
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserInfo userInfo = UserContextHolder.getUser();
        Long id = 0L;
        if (Objects.nonNull(userInfo)) {
            id = userInfo.getId();
        }
        this.strictUpdateFill(metaObject, "updateBy", Long.class, id);
        this.strictUpdateFill(metaObject, "updateAt", LocalDateTime.class, LocalDateTime.now());    
    }
}

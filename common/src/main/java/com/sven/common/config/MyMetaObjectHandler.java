package com.sven.common.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sven.common.constant.UserApplicationContext;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createBy", Long.class, UserApplicationContext.getUser().getId());
        this.strictInsertFill(metaObject, "createAt", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateBy", Long.class, UserApplicationContext.getUser().getId());
        this.strictInsertFill(metaObject, "updateAt", LocalDateTime.class, LocalDateTime.now()); 
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateBy", Long.class, UserApplicationContext.getUser().getId());
        this.strictUpdateFill(metaObject, "updateAt", LocalDateTime.class, LocalDateTime.now());    
    }
}

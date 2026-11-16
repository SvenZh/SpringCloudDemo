package com.sven.common.security;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableMethodSecurity
@Import({ ResourceServerAutoConfig.class, ResourceServerConfig.class })
public @interface EnableResourceServer {

}

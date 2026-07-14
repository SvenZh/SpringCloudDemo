package com.sven.auth.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.sven.auth.provider.CustomDaoAuthenticationProvider;

@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 白名单
                        .antMatchers("/test/captcha", "/test/login").permitAll()
                        // 其它请求都需要认证
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                // .rememberMe(rememberMe -> {
                //     rememberMe
                //             .tokenValiditySeconds(60 * 60)
                //             .rememberMeParameter("remember-me");
                // })
                .formLogin(Customizer.withDefaults())
                // .formLogin(formLogin -> {
                //     formLogin
                //         .loginPage("/test/login")                       // GET请求，展示自定义登录页面
                //         .loginProcessingUrl("/login")                   // POST请求，Spring Security自动处理登录验证
                //         .failureHandler((request, response, exception) -> {
                //             response.sendRedirect("/test/login?error=true");
                //         })         
                //         .successHandler((request, response, authentication) -> {        
                //             response.sendRedirect("http://127.0.0.1:10030/oauth2/authorize?response_type=code&client_id=testClient&scope=all&redirect_uri=https://www.baidu.com");
                //         });
                // })
                .headers(headers -> headers
                                .frameOptions(frameOptionsCustomizer -> frameOptionsCustomizer.sameOrigin())
                                .cacheControl(cacheControl -> cacheControl.disable()))
                .logout(Customizer.withDefaults())
                ;

        httpSecurity.authenticationProvider(new CustomDaoAuthenticationProvider());
        return httpSecurity.build();
    }
    
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache(cache -> cache.disable())
                .securityContext(context -> context.disable())
                .sessionManagement(management -> management.disable());
        
        return httpSecurity.build();
    }
}

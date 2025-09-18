package com.sven.system.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sven.common.security.NoToken;

@Controller
public class LoginController {
    @NoToken
    @GetMapping("/home")
    public String homePage(Model model,
            @AuthenticationPrincipal OAuth2User oauth2User,
            Authentication authentication) {

        if (oauth2User != null) {
            model.addAttribute("userName", oauth2User.getName());
            model.addAttribute("userAttributes", oauth2User.getAttributes());
            model.addAttribute("loginType", "OAuth2");
        } else if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("userName", authentication.getName());
            model.addAttribute("loginType", "Form");
        }

        return "home";
    }

    @NoToken
    @GetMapping("/login")
    public String loginPage(Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("errorMessage", "登录失败，请重试！");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "您已成功退出登录！");
        }

        return "login";
    }

    @GetMapping("/login/oauth2/code/myResourceServer")
    public void oauth2Callback() {
    }

    @NoToken
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // 获取错误信息
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        if (status != null) {
            model.addAttribute("status", status.toString());
        }
        if (exception != null) {
            model.addAttribute("error", exception.toString());
        }
        
        return "error";
    }
}

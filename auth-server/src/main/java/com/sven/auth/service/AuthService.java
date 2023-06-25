package com.sven.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sven.auth.vo.CaptchVO;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

@Service
public class AuthService {

    public CaptchVO getCaptcha() {
        SpecCaptcha captcha = new SpecCaptcha(150, 40, 5);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        captcha.setCharType(Captcha.FONT_9);
        String key = UUID.randomUUID().toString().replace("-", "");
        String image = captcha.toBase64();

        return new CaptchVO(image, key);
    }
}

package com.sven.auth.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.sven.auth.vo.CaptchVO;
import com.sven.common.constant.AppConstant;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

@Service
@RefreshScope
public class AuthService {

    @Value("${auth-server.captcha.length}")
    private int captchaLen;
    
    @Value("${auth-server.captcha.expire}")
    private long expire;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    public CaptchVO getCaptcha() {
        SpecCaptcha captcha = new SpecCaptcha(150, 40, captchaLen);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        captcha.setCharType(Captcha.FONT_9);
        String uuId = UUID.randomUUID().toString().replace("-", "");
        String image = captcha.toBase64();
        
        stringRedisTemplate.opsForValue().setIfAbsent(AppConstant.VALIDATION_CODE_PREFIX + uuId,
                captcha.text().toLowerCase(), expire, TimeUnit.SECONDS);
        
        return new CaptchVO(image, uuId);
    }
}

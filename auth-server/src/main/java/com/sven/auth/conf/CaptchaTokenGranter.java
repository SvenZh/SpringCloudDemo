package com.sven.auth.conf;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import com.sven.common.constant.AppConstant;

public class CaptchaTokenGranter extends AbstractTokenGranter{

    private static final String GRANT_TYPE = "captcha";
    private final AuthenticationManager authenticationManager;
    private StringRedisTemplate redisTemplate;

    public CaptchaTokenGranter(AuthenticationManager authenticationManager,
            AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory, StringRedisTemplate redisTemplate) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE, redisTemplate);
    }

    protected CaptchaTokenGranter(AuthenticationManager authenticationManager,
            AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory, String grantType, StringRedisTemplate redisTemplate) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String validationCode = parameters.get("validateCode");
        String uuid = parameters.get("uuId");
        Assert.isTrue(!StringUtils.isEmpty(validationCode), "验证码不能为空");

        String validateCodeKey = AppConstant.VALIDATION_CODE_PREFIX + uuid;
        String correctValidationCode = redisTemplate.opsForValue().get(validateCodeKey);
        
        if(!validationCode.equals(correctValidationCode)) {
            throw new InvalidGrantException("验证码错误!");
        } else {
            redisTemplate.delete(validateCodeKey);
        }
        
        String username = parameters.get("username");
        String password = parameters.get("password");
        
		parameters.remove("password");
		parameters.remove("uuId");
		parameters.remove("validateCode");

		Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);
		try {
			userAuth = authenticationManager.authenticate(userAuth);
		}
		catch (AccountStatusException ase) {
			throw new InvalidGrantException(ase.getMessage());
		}
		catch (BadCredentialsException e) {
			throw new InvalidGrantException(e.getMessage());
		}
		if (userAuth == null || !userAuth.isAuthenticated()) {
			throw new InvalidGrantException("Could not authenticate user: " + username);
		}

		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);		
		return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}

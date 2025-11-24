package com.sven.auth.service;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import com.sven.auth.vo.CaptchVO;
import com.sven.common.constant.AppConstant;
import com.sven.common.dto.RegClientDTO;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

@Service
@RefreshScope
public class AuthService {

    @Value("${captcha.length}")
    private int captchaLen;
    
    @Value("${captcha.expire}")
    private long expire;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
    
    public Boolean registeredClient(RegClientDTO dto) {
        TokenSettings tokenSettings = TokenSettings.builder()
                .authorizationCodeTimeToLive(Duration.ofMinutes(30))
                .accessTokenTimeToLive(Duration.ofMinutes(30))
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .reuseRefreshTokens(true)
                .refreshTokenTimeToLive(Duration.ofMinutes(60))
                .build();
        
        ClientSettings clientSettings = ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
        if (dto.getTokenSettings() != null) {
            tokenSettings = TokenSettings.builder()
                    .authorizationCodeTimeToLive(Duration.ofMinutes(dto.getTokenSettings().getAuthorizationCodeTimeToLive()))
                    .accessTokenTimeToLive(Duration.ofMinutes(dto.getTokenSettings().getAccessTokenTimeToLive()))
                    .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                    .reuseRefreshTokens(dto.getTokenSettings().getReuseRefreshTokens())
                    .refreshTokenTimeToLive(Duration.ofMinutes(dto.getTokenSettings().getRefreshTokenTimeToLive()))
                    .build();
        }
        
        if (dto.getClientSettings() != null) {
            clientSettings = ClientSettings.builder()
                    .requireAuthorizationConsent(dto.getClientSettings().getRequireAuthorizationConsent())
                    .build();
        }
        
        Set<AuthorizationGrantType> grantTypes = dto.getGrantType().stream()
                .map(grantType -> new AuthorizationGrantType(grantType)).collect(Collectors.toSet());
        
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        RegisteredClient repositoryByClientId = registeredClientRepository
                .findByClientId(dto.getClientId());
        String id = UUID.randomUUID().toString();
        if (Objects.nonNull(repositoryByClientId)) {
            id = repositoryByClientId.getId();
        }

        RegisteredClient registeredClient = RegisteredClient.withId(id).clientId(dto.getClientId())
                .clientSecret(passwordEncoder.encode(dto.getClientSecret()))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes((grantType) -> grantType.addAll(grantTypes))
                .redirectUri(dto.getRedirectUri())
                .scopes((scope) -> scope.addAll(dto.getScopes()))
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();

        registeredClientRepository.save(registeredClient);
        return true;
    }
}

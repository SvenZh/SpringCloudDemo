package com.sven.common.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegClientDTO {

    @NotEmpty(message = "不能为空")
    private String clientId;
    
    @NotEmpty(message = "不能为空")
    private Set<String> scopes;
    
    @NotEmpty(message = "不能为空")
    private Set<String> grantType;
    
    @NotEmpty(message = "不能为空")
    private String redirectUri;
    
    private TokenSettings tokenSettings;
    
    private ClientSettings clientSettings;
    
    @Getter
    @Setter
    public static class TokenSettings {
        
        private Integer oAuth2TokenFormat;
        
        private Integer authorizationCodeTimeToLive;
        
        private Integer accessTokenTimeToLive;
        
        private Boolean reuseRefreshTokens;
        
        private Integer refreshTokenTimeToLive;
    }
    
    @Getter
    @Setter
    public static class ClientSettings {
        private Boolean requireAuthorizationConsent;
    }
}

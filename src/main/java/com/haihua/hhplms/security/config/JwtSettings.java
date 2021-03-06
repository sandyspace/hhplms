package com.haihua.hhplms.security.config;

import com.haihua.hhplms.security.model.token.JwtToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtSettings {
    /**
     * {@link JwtToken} will expire after this time.
     */
    private Integer tokenExpirationTime;

    /**
     * Token issuer.
     */
    private String tokenIssuer;

    /**
     * Key is used to sign {@link com.haihua.hhplms.security.model.token.AccessJwtToken}.
     */
    private String tokenSigningKey;

    /**
     * Key is used to sign {@link com.haihua.hhplms.security.model.token.RefreshToken}.
     */
    private String refreshTokenSigningKey;

    /**
     * {@link JwtToken} can be refreshed during this timeframe.
     */
    private Integer refreshTokenExpTime;

    public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }

    public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(Integer tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public String getRefreshTokenSigningKey() {
        return refreshTokenSigningKey;
    }

    public void setRefreshTokenSigningKey(String refreshTokenSigningKey) {
        this.refreshTokenSigningKey = refreshTokenSigningKey;
    }
}

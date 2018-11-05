package com.haihua.hhplms.security.auth.jwt;

import com.haihua.hhplms.security.config.JwtSettings;
import com.haihua.hhplms.security.model.UserContext;
import com.haihua.hhplms.security.model.token.JwtToken;
import com.haihua.hhplms.security.model.token.RawAccessJwtToken;
import com.haihua.hhplms.security.auth.ajax.AjaxAuthenticationService;
import com.haihua.hhplms.security.auth.ajax.WebBasedAjaxAuthenticationService;
import com.haihua.hhplms.security.config.JwtSettings;
import com.haihua.hhplms.security.model.UserContext;
import com.haihua.hhplms.security.model.token.JwtToken;
import com.haihua.hhplms.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link JwtToken} to perform authentication.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;
    private final AjaxAuthenticationService ajaxAuthenticationService;
    private final WebBasedAjaxAuthenticationService webBasedAjaxAuthenticationService;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings,
                                     @Qualifier("employeeService") AjaxAuthenticationService ajaxAuthenticationService,
                                     @Qualifier("accountService") WebBasedAjaxAuthenticationService webBasedAjaxAuthenticationService) {
        this.jwtSettings = jwtSettings;
        this.ajaxAuthenticationService = ajaxAuthenticationService;
        this.webBasedAjaxAuthenticationService = webBasedAjaxAuthenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String userType = jwsClaims.getBody().get("userType", String.class);
        UserContext userContext;
        if (userType.equals("employee")) {
            userContext = ajaxAuthenticationService.loadUserContextByUsername(jwsClaims.getBody().getSubject());
        } else {
            userContext = webBasedAjaxAuthenticationService.loadUserContextByUsername(jwsClaims.getBody().getSubject());
        }
        return new JwtAuthenticationToken(userContext, AuthorityUtils.createAuthorityList(userContext.getUserProfile().getRoles().stream()
                .map(role -> role.getCode())
                .toArray(String[]::new)));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

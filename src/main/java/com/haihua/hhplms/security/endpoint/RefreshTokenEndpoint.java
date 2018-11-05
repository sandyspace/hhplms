package com.haihua.hhplms.security.endpoint;

import com.haihua.hhplms.security.model.UserBasicInfo;
import com.haihua.hhplms.security.model.token.JwtToken;
import com.haihua.hhplms.security.model.token.JwtTokenFactory;
import com.haihua.hhplms.security.model.token.RawAccessJwtToken;
import com.haihua.hhplms.security.model.token.RefreshToken;
import com.haihua.hhplms.security.auth.ajax.AjaxAuthenticationService;
import com.haihua.hhplms.security.auth.ajax.WebBasedAjaxAuthenticationService;
import com.haihua.hhplms.security.auth.jwt.extractor.TokenExtractor;
import com.haihua.hhplms.security.auth.jwt.verifier.TokenVerifier;
import com.haihua.hhplms.security.config.JwtSettings;
import com.haihua.hhplms.security.config.WebSecurityConfig;
import com.haihua.hhplms.security.exception.InvalidJwtToken;
import com.haihua.hhplms.security.model.UserBasicInfo;
import com.haihua.hhplms.security.model.token.JwtToken;
import com.haihua.hhplms.security.model.token.JwtTokenFactory;
import com.haihua.hhplms.security.model.token.RawAccessJwtToken;
import com.haihua.hhplms.security.model.token.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RefreshTokenEndpoint {
    @Autowired
    private JwtTokenFactory tokenFactory;

    @Autowired
    private JwtSettings jwtSettings;

    @Autowired
    private TokenVerifier tokenVerifier;

    @Autowired
    @Qualifier("jwtHeaderTokenExtractor")
    private TokenExtractor tokenExtractor;

    @Autowired
    @Qualifier("employeeService")
    private AjaxAuthenticationService ajaxAuthenticationService;

    @Autowired
    @Qualifier("accountService")
    private WebBasedAjaxAuthenticationService webBasedAjaxAuthenticationService;

    @RequestMapping(value="/api/auth/token", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public @ResponseBody
    JwtToken refreshToken(HttpServletRequest request) {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String username = refreshToken.getClaims().getBody().getSubject();
        String userType = refreshToken.getClaims().getBody().get("userType", String.class);
        UserBasicInfo userBasicInfo;
        if (userType.equals("employee")) {
            userBasicInfo = ajaxAuthenticationService.loadUserBasicInfoByUserName(username);
        } else {
            userBasicInfo = webBasedAjaxAuthenticationService.loadUserBasicInfoByUserName(username);
        }
        return tokenFactory.createAccessJwtToken(username, userBasicInfo.getType());
    }
}

package com.haihua.hhplms.security.config;

import com.haihua.hhplms.security.CustomCorsFilter;
import com.haihua.hhplms.security.RestAuthenticationEntryPoint;
import com.haihua.hhplms.security.auth.jwt.JwtAuthenticationProvider;
import com.haihua.hhplms.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.haihua.hhplms.security.auth.jwt.SkipPathRequestMatcher;
import com.haihua.hhplms.security.auth.jwt.extractor.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String ACCOUNT_AUTHENTICATION_URL = "/api/auth/account/login";
    public static final String ACCOUNT_FAST_AUTHENTICATION_URL = "/api/auth/account/fastLogin";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String FEEDBACK_CREATION_URL = "/api/sys/feedbacks";
    public static final String ACCOUNT_REGISTER_URL = "/api/auth/account/register";
    public static final String LATEST_PREFERENTIAL_MSG_URL = "/api/pm/preferentialMsgs/latest";
    public static final String API_ROOT_URL = "/api/**";

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private TokenExtractor tokenExtractor;
    @Autowired
    private AuthenticationManager authenticationManager;

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
                = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(AUTHENTICATION_URL, ACCOUNT_AUTHENTICATION_URL, ACCOUNT_FAST_AUTHENTICATION_URL, REFRESH_TOKEN_URL, FEEDBACK_CREATION_URL, ACCOUNT_REGISTER_URL, LATEST_PREFERENTIAL_MSG_URL, "/console");

        http.csrf().disable() // We don't need CSRF for JWT based authentication
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(API_ROOT_URL).authenticated() // Protected API End-points
                .and()
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, API_ROOT_URL),
                        UsernamePasswordAuthenticationFilter.class);
    }
}

package com.totoro.config;

import com.totoro.entity.RestBean;
import com.totoro.entity.dto.Account;
import com.totoro.entity.vo.response.AuthorizeVo;
import com.totoro.filter.JwtAuthorizeFilter;
import com.totoro.service.AccountService;
import com.totoro.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: totoro
 * @createDate: 2023 09 09 16 14
 * @description:
 **/
@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    AccountService accountService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**","/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDeny)
                )
                .csrf(conf -> conf.disable())
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");

        User user = (User) authentication.getPrincipal();

        Account account = accountService.findAccountByNameOrEmail(user.getUsername());

        String token = jwtUtils.createJwt(user, Long.valueOf(account.getId()), account.getUsername());

        AuthorizeVo vo = new AuthorizeVo();

        vo.setExpire(jwtUtils.expireTime());
        vo.setToken(token);
        vo.setUsername(account.getUsername());
        vo.setRole(account.getRole());

        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    //拒绝访问
    private void onAccessDeny(HttpServletRequest request
            , HttpServletResponse response
            , AccessDeniedException e) throws IOException {

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(RestBean.forbidden(e.getMessage()).asJsonString());

    }


    //未授权
    public void onUnauthorized(HttpServletRequest request
            , HttpServletResponse response
            , AuthenticationException exception) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }



    //认证失败
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }

    //退出登录
    public void onLogoutSuccess(HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (jwtUtils.invalidateJwt(authorization)){
            writer.write(RestBean.success().asJsonString());
        } else {
            writer.write(RestBean.failure(400,"退出登录失败").asJsonString());
        }
    }
}

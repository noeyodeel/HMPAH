package com.sparta.hmpah.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hmpah.dto.requestDto.LoginRequest;
import com.sparta.hmpah.entity.UserRoleEnum;
import com.sparta.hmpah.jwt.JwtMessage;
import com.sparta.hmpah.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final JwtMessage jwtMessage;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, JwtMessage jwtMessage) {
        this.jwtUtil = jwtUtil;
        this.jwtMessage = jwtMessage;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest requestDto = new ObjectMapper().readValue(request.getInputStream(),
                LoginRequest.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username,role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        jwtMessage.messageToClient(response, 200, "로그인 성공", "success");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException {

        jwtMessage.messageToClient(response, 400, "로그인 실패", "error");

    }

}

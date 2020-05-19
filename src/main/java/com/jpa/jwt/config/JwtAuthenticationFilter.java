package com.jpa.jwt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

/**
 * @author hyunjae
 * jwt filter 설정
 *
 */
@RequiredArgsConstructor
// GenericFilterBean : spring filter config 설정을 편하게 처리하기 위한 추상 클래스
public class JwtAuthenticationFilter extends GenericFilter{
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // requset header에서 토큰 정보 가져오기
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인
		if (token != null && jwtTokenProvider.validateToken(token)) {
            // 유저정보를 받아옴
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext에 Authentication 객체 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}

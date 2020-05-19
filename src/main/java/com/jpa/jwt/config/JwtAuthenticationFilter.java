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
 * jwt filter ����
 *
 */
@RequiredArgsConstructor
// GenericFilterBean : spring filter config ������ ���ϰ� ó���ϱ� ���� �߻� Ŭ����
public class JwtAuthenticationFilter extends GenericFilter{
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // requset header���� ��ū ���� ��������
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // ��ȿ�� ��ū���� Ȯ��
		if (token != null && jwtTokenProvider.validateToken(token)) {
            // ���������� �޾ƿ�
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext�� Authentication ��ü ����
			SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}

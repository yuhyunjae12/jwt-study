package com.jpa.jwt.config;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

/**
 * @author hyunjae
 * Jwt Provider
 *
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	// 암호화 키 선언
	private String secretKey = "jwtKey";
	
	// 토큰 유효시간 설정
	private long tokenVaildTime = 30 * 60 * 1000L;
	
	private final UserDetailsService userDetailsService;
	
	/*
	 * @PostConstruct : was가 구동 될때 실행됨
	 * 암호화 키를 초기화
	 */
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	// jwt 토큰 생성
	// jwt 는 Claim 기반으로 정보를 담고 있는 토큰
	public String createToken(String userPk, List<String> role) {
		Claims claims = Jwts.claims().setSubject(userPk); // jwt payload에 저장되는 정보 단위
		claims.put("roles", role); // key/ value 형식으로 저장됩니다.
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims) // 토큰 정보 저장
				.setIssuedAt(now) // 토큰 발생 시간
				.setExpiration(new Date(now.getTime() + tokenVaildTime)) // 토큰 유효시간
				.signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 시크릿키 값 세팅
				.compact();
	}
	
	// jwt 인증정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // jwt에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // request header에서 토큰 정보를 가져옵니다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰 유효성 + 유효시간 검증
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
}

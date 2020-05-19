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

	// ��ȣȭ Ű ����
	private String secretKey = "jwtKey";
	
	// ��ū ��ȿ�ð� ����
	private long tokenVaildTime = 30 * 60 * 1000L;
	
	private final UserDetailsService userDetailsService;
	
	/*
	 * @PostConstruct : was�� ���� �ɶ� �����
	 * ��ȣȭ Ű�� �ʱ�ȭ
	 */
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	// jwt ��ū ����
	// jwt �� Claim ������� ������ ��� �ִ� ��ū
	public String createToken(String userPk, List<String> role) {
		Claims claims = Jwts.claims().setSubject(userPk); // jwt payload�� ����Ǵ� ���� ����
		claims.put("roles", role); // key/ value �������� ����˴ϴ�.
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims) // ��ū ���� ����
				.setIssuedAt(now) // ��ū �߻� �ð�
				.setExpiration(new Date(now.getTime() + tokenVaildTime)) // ��ū ��ȿ�ð�
				.signWith(SignatureAlgorithm.HS256, secretKey) // ����� ��ȣȭ �˰���� ��ũ��Ű �� ����
				.compact();
	}
	
	// jwt �������� ��ȸ
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // jwt���� ȸ�� ���� ����
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // request header���� ��ū ������ �����ɴϴ�.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // ��ū ��ȿ�� + ��ȿ�ð� ����
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
}

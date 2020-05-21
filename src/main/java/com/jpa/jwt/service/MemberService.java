package com.jpa.jwt.service;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jpa.jwt.config.JwtTokenProvider;
import com.jpa.jwt.entity.MemberEntity;
import com.jpa.jwt.repository.MemberRepository;
import com.jpa.jwt.vo.MemberVo;

import lombok.RequiredArgsConstructor;

/**
 * @author hyunjae
 * 
 * 회원 서비스
 *
 */
@RequiredArgsConstructor
@Service
public class MemberService{

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
	
	public Long join(final MemberVo vo){
		return memberRepository.save(MemberEntity.builder()
                .email(vo.getEmail())
                .password(passwordEncoder.encode(vo.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
	}
	
	public String login(final MemberVo vo) {
		MemberEntity member = memberRepository.findByEmail(vo.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
		if (!passwordEncoder.matches(vo.getPassword(), member.getPassword())) {
			throw new IllegalArgumentException("잘못된 비밀번호입니다.");
		}
		
		return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
	}
	
}

package com.jpa.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jpa.jwt.config.JwtTokenProvider;
import com.jpa.jwt.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

//RequiredArgsConstructor 는 final 필드를 생성자 주입해주는 lombok 어노테이션
@RequiredArgsConstructor
@Service
public class UsersService implements UserDetailsService{

	private final MemberRepository memberRepository;
	
	// UserDetailsService는 UserDetails를 통해 회원정보를 담은 객체를 가져오는 인터 페이스 
	// loadUserByUsername 구현 메소드를 통해 유저 정보를 불러오게 됩니다.
	// 회원 이메일 ( 아이디 )를 통해 회원을 찾습니다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
	}
}

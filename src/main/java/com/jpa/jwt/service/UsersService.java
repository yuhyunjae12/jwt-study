package com.jpa.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jpa.jwt.config.JwtTokenProvider;
import com.jpa.jwt.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

//RequiredArgsConstructor �� final �ʵ带 ������ �������ִ� lombok ������̼�
@RequiredArgsConstructor
@Service
public class UsersService implements UserDetailsService{

	private final MemberRepository memberRepository;
	
	// UserDetailsService�� UserDetails�� ���� ȸ�������� ���� ��ü�� �������� ���� ���̽� 
	// loadUserByUsername ���� �޼ҵ带 ���� ���� ������ �ҷ����� �˴ϴ�.
	// ȸ�� �̸��� ( ���̵� )�� ���� ȸ���� ã���ϴ�.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("����ڸ� ã�� �� �����ϴ�."));
	}
}

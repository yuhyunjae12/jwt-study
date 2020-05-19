package com.jpa.jwt.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.jwt.config.JwtTokenProvider;
import com.jpa.jwt.entity.MemberEntity;
import com.jpa.jwt.repository.MemberRepository;
import com.jpa.jwt.vo.MemberVo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {
	
	private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("/join")
    public Long join(@RequestBody MemberVo vo) {
        return memberRepository.save(MemberEntity.builder()
                .email(vo.getEmail())
                .password(passwordEncoder.encode(vo.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
    }

    @PostMapping("/login")
    public String login(@RequestBody MemberVo vo) {
    	MemberEntity member = memberRepository.findByEmail(vo.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(vo.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
    
    @GetMapping("/user/test")
    public String userTest() {
    	return "user";
    }
    
    @GetMapping("/admin/test")
    public String adminTest() {
    	return "admin";
    }
}

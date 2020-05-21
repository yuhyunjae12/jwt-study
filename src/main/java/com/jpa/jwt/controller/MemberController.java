package com.jpa.jwt.controller;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.jwt.config.JwtTokenProvider;
import com.jpa.jwt.entity.MemberEntity;
import com.jpa.jwt.repository.MemberRepository;
import com.jpa.jwt.service.MemberService;
import com.jpa.jwt.vo.MemberVo;

import lombok.RequiredArgsConstructor;

/**
 * @author hyunjae
 * 회원 컨트롤러
 */
@RequiredArgsConstructor
@RestController
public class MemberController {
	
	private final MemberService memberService;
	
    @PostMapping("/join")
    public ResponseEntity<Long> join(@RequestBody final MemberVo vo) {
        return new ResponseEntity<Long>(memberService.join(vo), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody final MemberVo vo) {
    	return new ResponseEntity<String>(memberService.login(vo), HttpStatus.OK);
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

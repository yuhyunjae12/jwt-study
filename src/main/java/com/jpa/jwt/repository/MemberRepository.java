package com.jpa.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.jwt.entity.MemberEntity;

// 회원 엔티티를 정의한 MemberEntity repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

	// 회원 이메일을 통해 사용자 정보를 가져오는 findByEmail를 선언
	Optional<MemberEntity> findByEmail(String email);
	
}

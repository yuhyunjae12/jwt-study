package com.jpa.jwt.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author hyunjae
 * 
 * 회원 엔티티 정의
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
/**
 * 
 * @Getter : getter 생성을 위한 lombok 어노테이션
 * @NoArgsConstructor : 파라미터가 없는 기본 생성자를 만들어주는 lombok 어노테이션
 * @AllArgsConstructor : 모든 필드를 받는 생성자를 만들어주는 lombok 어노테이션
 * @Builder : 빌더 패턴을 조금더 간편하게 쓸 수 있도록 해주는 lombok 어노테이션
 * @Entity : 클래스 엔티티를 정의 하는 jpa 어노테이션
 * UserDetails : 스프링 시큐리티에서 회원 정보를 담는 인터페이스
 */
public class MemberEntity implements UserDetails{

	/**
	 * @Id : 필드를 pk(id)로 사용하겠다는 선언
	 * @GeneratedValue : 자동으로 증가하는 필드 선언
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	/**
	 * 회원 이메일
	 */
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    /**
     * 회원 비밀번호
     */
    @Column(length = 300, nullable = false)
    private String password;

    /**
     * role 
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

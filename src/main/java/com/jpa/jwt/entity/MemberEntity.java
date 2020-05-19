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
 * ȸ�� ��ƼƼ ����
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
/**
 * 
 * @Getter : getter ������ ���� lombok ������̼�
 * @NoArgsConstructor : �Ķ���Ͱ� ���� �⺻ �����ڸ� ������ִ� lombok ������̼�
 * @AllArgsConstructor : ��� �ʵ带 �޴� �����ڸ� ������ִ� lombok ������̼�
 * @Builder : ���� ������ ���ݴ� �����ϰ� �� �� �ֵ��� ���ִ� lombok ������̼�
 * @Entity : Ŭ���� ��ƼƼ�� ���� �ϴ� jpa ������̼�
 * UserDetails : ������ ��ť��Ƽ���� ȸ�� ������ ��� �������̽�
 */
public class MemberEntity implements UserDetails{

	/**
	 * @Id : �ʵ带 pk(id)�� ����ϰڴٴ� ����
	 * @GeneratedValue : �ڵ����� �����ϴ� �ʵ� ����
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	/**
	 * ȸ�� �̸���
	 */
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    /**
     * ȸ�� ��й�ȣ
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

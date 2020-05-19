package com.jpa.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * @author hyunjae
 * 
 * ������ ��ť��Ƽ ���Ǳ� ����
 *
 */
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // ��ť��Ƽ �⺻ ���� ������� ����
                .csrf().disable()  // csrf�� ������� ����
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ������ ������� ���� 
                .and()
                .authorizeRequests() 
                .antMatchers("/admin/**").hasRole("ADMIN") //���� ����
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll() 
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); //  filter�� �ۼ��� JwtAuthenticationFilter�� ����
    }
    
}

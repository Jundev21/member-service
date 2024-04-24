package com.commerce.memberservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.commerce.memberservice.domain.member.service.MemberService;
import com.commerce.memberservice.jwt.JwtFilter;
import com.commerce.memberservice.jwt.JwtTokenInfo;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenInfo jwtTokenInfo;
	private final MemberService memberService;


	@Bean
	public BCryptPasswordEncoder encoderPassword(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
			.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(CsrfConfigurer::disable)
			.cors(CorsConfigurer::disable)
			.formLogin(FormLoginConfigurer::disable)
			.authorizeHttpRequests(request -> request
				.antMatchers("/", "/api/user/join", "/api/user/login").permitAll()
				.anyRequest().authenticated())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterAt(new JwtFilter(jwtTokenInfo,memberService), UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
}

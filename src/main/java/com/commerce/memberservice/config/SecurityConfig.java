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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.commerce.memberservice.common.exception.SecurityException.UnAuthorizedException;
import com.commerce.memberservice.filter.JwtFilter;
import com.commerce.memberservice.filter.auth.MemberDetailService;
import com.commerce.memberservice.jwt.JwtTokenInfo;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenInfo jwtTokenInfo;
	private final MemberDetailService memberDetailService;
	private final UnAuthorizedException unAuthorizedException;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
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
			.addFilterBefore(new JwtFilter(jwtTokenInfo, memberDetailService),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(
				exceptionHandling -> exceptionHandling
					.authenticationEntryPoint(unAuthorizedException)
			);



		return httpSecurity.build();
	}

}

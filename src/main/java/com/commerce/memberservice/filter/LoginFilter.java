package com.commerce.memberservice.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// UsernamePasswordFilter 는 spring security 에서 기본적으로 제공되는 로그인페이지에서 사용자 아이디와 비밀번호를 가로체서
// AuthenticationManager 에게 아이디와 비밀번호를 전달하고 디비에 있는 유져있지 회원을 인증한다.
// JWT 는 기본 로그인페이를 사용하지않음으로 UsernamePasswordFilter 를 사용하지않게되는데
// 위같은 플로우를 사용하기위해서 UsernamePasswordFilter 를 커스텀으로 제작해야한다.
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		String username = this.obtainUsername(request);
		String password = this.obtainPassword(request);


		UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);


		return super.attemptAuthentication(request, response);
	}
}

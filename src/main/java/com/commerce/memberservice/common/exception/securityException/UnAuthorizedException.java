package com.commerce.memberservice.common.exception.securityException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.commerce.memberservice.common.basicResponse.DataResponse;
import com.commerce.memberservice.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnAuthorizedException implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
	) throws IOException, ServletException {
		HttpStatus errorCode = null;
		String errorMessage = null;
		if(authException instanceof BadCredentialsException || authException instanceof InternalAuthenticationServiceException){
			errorMessage = ErrorCode.INVALID_LOGIN_INFO.getMsg();
			errorCode = ErrorCode.INVALID_LOGIN_INFO.getStatus();
		} else if(authException instanceof CredentialsExpiredException){
			errorMessage = ErrorCode.INVALID_TOKEN.getMsg();
			errorCode = ErrorCode.INVALID_TOKEN.getStatus();
		}else{
			errorMessage = ErrorCode.UNAUTHORIZED_USER.getMsg();
			errorCode = ErrorCode.UNAUTHORIZED_USER.getStatus();

		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(
				objectMapper.writeValueAsString(
					DataResponse.failBodyResponse(errorCode, errorMessage)
				)
			);
		log.error(errorMessage +" " +errorCode);
	}
}
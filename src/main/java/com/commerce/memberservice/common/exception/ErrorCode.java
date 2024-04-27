package com.commerce.memberservice.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT,"존재하는 회원이 있습니다."),
	NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다."),
	INVALID_LOGIN_INFO(HttpStatus.BAD_REQUEST,"로그인 정보가 다릅니다. 다시확인해주세요."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다."),
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED,"사용자 권한이 없습니다.");


	public final HttpStatus status;
	private final String msg;
	ErrorCode(HttpStatus status, String msg) {
		this.status=status;
		this.msg = msg;
	}
}

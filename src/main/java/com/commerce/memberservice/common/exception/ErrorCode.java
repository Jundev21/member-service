package com.commerce.memberservice.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	NotFoundMember(400, "존재하지 않는 회원입니다."),
	AlreadyExistMember(400, "이미 존재하는 회원입니다.");

	private final Integer code;
	private final String msg;
	ErrorCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}

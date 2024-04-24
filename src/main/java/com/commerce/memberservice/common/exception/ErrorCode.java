package com.commerce.memberservice.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	NotFoundMember(400, "Can't find member");

	private final Integer code;
	private final String msg;
	ErrorCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}

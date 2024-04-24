package com.commerce.memberservice.common.exception;

public class BasicException extends RuntimeException{

	protected final ErrorCode errorCode;

	public BasicException(ErrorCode errorCode){
		this.errorCode = errorCode;
	}

	public BasicException(ErrorCode errorCode, String message){
		super(message);
		this.errorCode = errorCode;
	}


}

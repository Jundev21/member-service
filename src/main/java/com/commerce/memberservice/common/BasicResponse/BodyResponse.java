package com.commerce.memberservice.common.BasicResponse;

import org.springframework.http.HttpStatus;

public class BodyResponse<T> extends BasicResponse{

	private final T responseBody;
	public BodyResponse(Integer statusCode, String message, T responseBody) {
		super(statusCode, message);
		this.responseBody = responseBody;
	}

	public static <T> BodyResponse<T> successBodyResponse(HttpStatus httpStatus, T responseBody){
		return new BodyResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), responseBody);
	}

	public static <T> BodyResponse<T> failBodyResponse(HttpStatus httpStatus, T responseBody){
		return new BodyResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), responseBody);
	}


}

package com.commerce.memberservice.common.BasicResponse;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DataResponse<T> extends BasicResponse{

	private final T responseData;
	public DataResponse(Integer statusCode, String message, T responseData) {
		super(statusCode, message);
		this.responseData = responseData;
	}

	public static <T> DataResponse<T> successBodyResponse(HttpStatus httpStatus, T responseData){
		return new DataResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), responseData);
	}

	public static <T> DataResponse<T> failBodyResponse(HttpStatus httpStatus, T responseData){
		return new DataResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), responseData);
	}


}

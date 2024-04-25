package com.commerce.memberservice.common.BasicResponse;

import javax.persistence.Basic;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicResponse{

	protected static final String SUCCESS_MSG = "요청에 성공하였습니다.";
	protected static final String FAIL_MSG = "요청에 실패하였습니다.";
	private Integer statusCode;
	private String message;

	public static BasicResponse basicSuccess(HttpStatus httpStatus){
		return new BasicResponse(httpStatus.value(),SUCCESS_MSG + " " + httpStatus.getReasonPhrase());
	}
	public static BasicResponse basicFail(HttpStatus httpStatus){
		return new BasicResponse(httpStatus.value(),FAIL_MSG+ " " + httpStatus.getReasonPhrase());
	}


}

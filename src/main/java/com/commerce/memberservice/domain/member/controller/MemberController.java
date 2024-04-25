package com.commerce.memberservice.domain.member.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.memberservice.common.BasicResponse.BasicResponse;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/join")
	public BasicResponse memberRegister(@RequestBody @Valid MemberRegisterDto memberRegisterInfo){
		memberService.memberRegister(memberRegisterInfo);
	return BasicResponse.basicSuccess(HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public BasicResponse memberLogin(@RequestBody @Valid MemberLoginDto memberLoginInfo){

		memberService.memberLogin(memberLoginInfo);
		return BasicResponse.basicSuccess(HttpStatus.OK);
	}


}

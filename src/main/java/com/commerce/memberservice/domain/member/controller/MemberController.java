package com.commerce.memberservice.domain.member.controller;

import java.awt.print.Pageable;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.memberservice.common.BasicResponse.BasicResponse;
import com.commerce.memberservice.common.BasicResponse.DataResponse;
import com.commerce.memberservice.domain.member.dto.Request.MemberEditInfoDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.dto.Response.LoginResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberEditInfoResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberListResponseDto;
import com.commerce.memberservice.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/join")
	public BasicResponse memberRegister(
		@RequestBody @Valid MemberRegisterDto memberRegisterInfo
	) {
		memberService.memberRegister(memberRegisterInfo);
		return BasicResponse.basicSuccess(HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public DataResponse<LoginResponseDto> memberLogin(
		@RequestBody @Valid MemberLoginDto memberLoginInfo
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.memberLogin(memberLoginInfo));
	}

	@PutMapping("/{loginId}")
	public DataResponse<MemberEditInfoResponseDto> memberEdit(
		@RequestBody @Valid MemberEditInfoDto memberEditInfoDto,
		@PathVariable String loginId
	) {
		return DataResponse.successBodyResponse(HttpStatus.ACCEPTED,
			memberService.memberEditInfo(loginId, memberEditInfoDto));
	}

	@GetMapping("/list")
	public DataResponse<MemberListResponseDto> memberList(
		@RequestParam(value = "page", required = false, defaultValue = "0") int pageNum,
		@RequestParam(value = "pageSize", required = false, defaultValue = "2") int pageSize,
		@RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK,
			memberService.memberList(PageRequest.of(pageNum, pageSize, Sort.by(sort).ascending())));
	}
}

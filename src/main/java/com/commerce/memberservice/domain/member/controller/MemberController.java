package com.commerce.memberservice.domain.member.controller;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.memberservice.common.basicResponse.BasicResponse;
import com.commerce.memberservice.common.basicResponse.DataResponse;
import com.commerce.memberservice.domain.member.dto.Request.MemberEditInfoDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.dto.Response.LoginResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberEditInfoResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberListResponseDto;
import com.commerce.memberservice.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@Tag(name = "Member Service API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "사용자 회원가입 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "회원가입 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "이미 존재하는 회원일 경우",
			responseCode = "409",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@PostMapping("/join")
	public BasicResponse memberRegister(
		@RequestBody @Valid MemberRegisterDto memberRegisterInfo
	) {
		memberService.memberRegister(memberRegisterInfo);
		return BasicResponse.basicSuccess(HttpStatus.CREATED);
	}

	@Operation(summary = "사용자 로그인 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "로그인 성공",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "비밀번호가 다를경우",
			responseCode = "400",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		),
		@ApiResponse(
			description = "토큰이 일치하지않거나 만료되었을 경우",
			responseCode = "401",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		),
	})
	@PostMapping("/login")
	public DataResponse<LoginResponseDto> memberLogin(
		@RequestBody @Valid MemberLoginDto memberLoginInfo
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, memberService.memberLogin(memberLoginInfo));
	}

	@Operation(summary = "회원 정보 변경 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "회원정보 변경 성공했을시",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "잘못된 형식으로 요청했을 때",
			responseCode = "400",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		),
		@ApiResponse(
			description = "권한이 없어 접근못할경우",
			responseCode = "401",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		),
	})
	@PutMapping("/{loginId}")
	public DataResponse<MemberEditInfoResponseDto> memberEdit(
		@RequestBody @Valid MemberEditInfoDto memberEditInfoDto,
		@PathVariable String loginId
	) {
		return DataResponse.successBodyResponse(HttpStatus.ACCEPTED,
			memberService.memberEditInfo(loginId, memberEditInfoDto));
	}

	@Operation(summary = "회원 정보 조회")
	@ApiResponses(value = {
		@ApiResponse(
			description = "정상적으로 호출했을 때",
			responseCode = "200",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "권한이 없어 접근못할경우",
			responseCode = "401",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		),
	})
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

package com.commerce.memberservice.domain.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.memberservice.common.UserRoles;
import com.commerce.memberservice.common.exception.BasicException;
import com.commerce.memberservice.common.exception.ErrorCode;
import com.commerce.memberservice.domain.member.dto.Request.MemberEditInfoDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.dto.Response.LoginResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberBasicResponse;
import com.commerce.memberservice.domain.member.dto.Response.MemberEditInfoResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberListResponseDto;
import com.commerce.memberservice.domain.member.entity.MemberEntity;
import com.commerce.memberservice.domain.member.repository.MemberRepository;
import com.commerce.memberservice.filter.auth.MemberDetail;
import com.commerce.memberservice.jwt.JwtTokenInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtTokenInfo jwtTokenInfo;
	private final AuthenticationManager authenticationManager;

	@Transactional
	public void memberRegister(MemberRegisterDto memberRegisterInfo) {
		checkExistMember(memberRegisterInfo.getMemberLoginId());
		MemberEntity memberEntity =
			new MemberEntity(
				memberRegisterInfo.getMemberName(),
				memberRegisterInfo.getMemberNickName(),
				memberRegisterInfo.getMemberLoginId(),
				bCryptPasswordEncoder.encode(memberRegisterInfo.getMemberPassword()),
				memberRegisterInfo.getMemberEmail(),
				memberRegisterInfo.getMemberPhoneNumber(),
				UserRoles.USER
			);
		memberRepository.save(memberEntity);
	}

	@Transactional
	public LoginResponseDto memberLogin(MemberLoginDto memberLoginInfo) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				memberLoginInfo.getLoginId(),
				memberLoginInfo.getPassword()
			)
		);
		MemberDetail memberDetail = (MemberDetail)authentication.getPrincipal();
		String generatedJwtToken = jwtTokenInfo.generateToken(memberDetail.getUsername());

		return LoginResponseDto.builder()
			.jwtToken(generatedJwtToken)
			.build();
	}

	@Transactional
	public MemberEditInfoResponseDto memberEditInfo(String loginId, MemberEditInfoDto memberEditInfoDto) {
		MemberEntity member = checkValidMemberByLoginId(loginId);
		member.updateMemberEntity(memberEditInfoDto);

		return MemberEditInfoResponseDto.builder()
			.memberName(member.getMemberName())
			.memberNickName(member.getMemberNickName())
			.memberLoginId(member.getMemberLoginId())
			.memberEmail(member.getMemberEmail())
			.memberPhoneNumber(member.getMemberPhoneNumber())
			.build();
	}

	@Transactional(readOnly = true)
	public MemberEntity checkValidMemberByLoginId(String loginId) {
		return memberRepository.findByMemberLoginId(loginId)
			.orElseThrow(
				(() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())));
	}

	@Transactional(readOnly = true)
	public MemberListResponseDto memberList(Pageable pageable) {
		Page<MemberEntity> memberList = memberRepository.findAll(pageable);
		List<MemberBasicResponse> memberBasicResponse
			= memberList.getContent().stream().map(MemberBasicResponse::fromEntity
		).collect(Collectors.toList());

		return MemberListResponseDto.builder()
			.memberList(memberBasicResponse)
			.pageNo(memberList.getNumber())
			.pageSize(memberList.getSize())
			.totalElements(memberList.getTotalElements())
			.totalPages(memberList.getTotalPages())
			.build();
	}

	@Transactional(readOnly = true)
	public void checkExistMember(String loginId) {
		Boolean isExistMember = memberRepository.existsByMemberLoginId(loginId);
		if (isExistMember) {
			throw new BasicException(ErrorCode.ALREADY_EXIST_MEMBER, ErrorCode.ALREADY_EXIST_MEMBER.getMsg());
		}
	}
}

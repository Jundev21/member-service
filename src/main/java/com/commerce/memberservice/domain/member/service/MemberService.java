package com.commerce.memberservice.domain.member.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.memberservice.common.UserRoles;
import com.commerce.memberservice.common.exception.BasicException;
import com.commerce.memberservice.common.exception.ErrorCode;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.dto.Response.LoginResponseDto;
import com.commerce.memberservice.domain.member.entity.MemberEntity;
import com.commerce.memberservice.domain.member.repository.MemberRepository;
import com.commerce.memberservice.filter.auth.MemberDetail;
import com.commerce.memberservice.filter.auth.MemberDetailService;
import com.commerce.memberservice.jwt.JwtTokenInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtTokenInfo jwtTokenInfo;
	private final AuthenticationManager authenticationManager;
	;

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
		System.out.println(generatedJwtToken);
		return LoginResponseDto.builder()
			.jwtToken(generatedJwtToken)
			.build();
	}

	@Transactional(readOnly = true)
	public MemberEntity checkValidMember(String loginId) {
		return memberRepository.findByMemberLoginId(loginId)
			.orElseThrow((() -> new BasicException(ErrorCode.NotFoundMember, ErrorCode.NotFoundMember.getMsg())));
	}

	@Transactional(readOnly = true)
	private void checkExistMember(String loginId) {
		Boolean isExistMember = memberRepository.existsByMemberLoginId(loginId);
		if (isExistMember) {
			throw new BasicException(ErrorCode.AlreadyExistMember, ErrorCode.AlreadyExistMember.getMsg());
		}
	}
}

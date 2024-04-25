package com.commerce.memberservice.domain.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.memberservice.common.UserRoles;
import com.commerce.memberservice.common.exception.BasicException;
import com.commerce.memberservice.common.exception.ErrorCode;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.entity.MemberEntity;
import com.commerce.memberservice.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


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


	@Transactional(readOnly = true)
	public void memberLogin(MemberLoginDto memberLoginInfo) {



		//로그인 성공하면 토큰 발급하여 반환



	}

	@Transactional(readOnly = true)
	public MemberEntity checkValidMember(String loginId) {
		return memberRepository.findByMemberLoginId(loginId)
			.orElseThrow((() -> new BasicException(ErrorCode.NotFoundMember, ErrorCode.NotFoundMember.getMsg())));
	}

	@Transactional(readOnly = true)
	private void checkExistMember(String loginId) {
		Boolean isExistMember = memberRepository.existsByMemberLoginId(loginId);
		if(isExistMember){
			throw new BasicException(ErrorCode.AlreadyExistMember, ErrorCode.AlreadyExistMember.getMsg());
		}
	}

}

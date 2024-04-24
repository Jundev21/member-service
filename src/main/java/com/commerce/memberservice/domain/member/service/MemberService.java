package com.commerce.memberservice.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.memberservice.common.exception.BasicException;
import com.commerce.memberservice.common.exception.ErrorCode;
import com.commerce.memberservice.domain.member.entity.MemberEntity;
import com.commerce.memberservice.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public MemberEntity checkValidMember(String loginId) {
		return memberRepository.findByMemberLoginId(loginId)
			.orElseThrow((() -> new BasicException(ErrorCode.NotFoundMember, ErrorCode.NotFoundMember.getMsg())));
	}

}

package com.commerce.memberservice.filter.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.commerce.memberservice.common.exception.BasicException;
import com.commerce.memberservice.common.exception.ErrorCode;
import com.commerce.memberservice.domain.member.entity.MemberEntity;
import com.commerce.memberservice.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		MemberEntity member= memberRepository.findByMemberLoginId(loginId)
			.orElseThrow((() -> new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())));
		return new MemberDetail(member);
	}
}

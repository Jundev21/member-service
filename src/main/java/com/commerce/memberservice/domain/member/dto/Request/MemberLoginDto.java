package com.commerce.memberservice.domain.member.dto.Request;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginDto {
	@NotBlank(message = "아이디는 필수 값 입니다.")
	private String loginId;
	@NotBlank(message = "비밀번호는 필수 값 입니다.")
	private String password;
}

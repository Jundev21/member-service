package com.commerce.memberservice.domain.member.dto.Request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditInfoDto {
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String memberName;
	@NotBlank(message = "닉네임은 필수 입력 값입니다.")
	private String memberNickName;
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
		message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
	private String memberPassword;
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "Email 형식이 아닙니다.")
	private String memberEmail;
	@NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
	@Pattern(regexp ="^\\d{2,3}-\\d{3,4}-\\d{4}$",message="핸드폰 번호 형식이 아닙니다.")
	private String memberPhoneNumber;
}

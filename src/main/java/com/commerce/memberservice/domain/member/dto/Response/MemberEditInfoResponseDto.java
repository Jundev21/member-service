package com.commerce.memberservice.domain.member.dto.Response;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditInfoResponseDto {
	private String memberName;
	private String memberNickName;
	private String memberLoginId;
	private String memberEmail;
	private String memberPhoneNumber;
	private LocalDateTime modifiedDate;
}

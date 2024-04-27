package com.commerce.memberservice.domain.member.dto.Response;

import java.time.LocalDateTime;

import com.commerce.memberservice.domain.member.entity.MemberEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBasicResponse {
	private String memberName;
	private String memberNickName;
	private String memberLoginId;
	private String memberEmail;
	private String memberPhoneNumber;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;

	public static MemberBasicResponse fromEntity(MemberEntity memberEntity) {
		return MemberBasicResponse.builder()
			.memberName(memberEntity.getMemberName())
			.memberNickName(memberEntity.getMemberNickName())
			.memberLoginId(memberEntity.getMemberLoginId())
			.memberEmail(memberEntity.getMemberEmail())
			.memberPhoneNumber(memberEntity.getMemberPhoneNumber())
			.createdDate(memberEntity.getCreatedDate())
			.lastModifiedDate(memberEntity.getModifiedDate())
			.build();
	}

}

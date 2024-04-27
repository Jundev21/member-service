package com.commerce.memberservice.domain.member.dto.Response;

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

		public static MemberBasicResponse fromEntity(MemberEntity MemberEntity){
			return MemberBasicResponse.builder()
				.memberName(MemberEntity.getMemberName())
				.memberNickName(MemberEntity.getMemberNickName())
				.memberLoginId(MemberEntity.getMemberLoginId())
				.memberEmail(MemberEntity.getMemberEmail())
				.memberPhoneNumber(MemberEntity.getMemberPhoneNumber())
				.build();
		}

}

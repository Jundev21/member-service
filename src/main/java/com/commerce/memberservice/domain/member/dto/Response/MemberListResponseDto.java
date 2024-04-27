package com.commerce.memberservice.domain.member.dto.Response;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberListResponseDto {
	private List<MemberBasicResponse> memberList;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private long totalPages;
}

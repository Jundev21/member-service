package com.commerce.memberservice.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.commerce.memberservice.domain.member.dto.Response.MemberListResponseDto;
import com.commerce.memberservice.domain.member.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

	Optional<MemberEntity> findByMemberLoginId(String loginId);
	Boolean existsByMemberLoginId(String loginId);
	Page<MemberEntity> findAll(Pageable pageable);

}

package com.commerce.memberservice.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commerce.memberservice.domain.member.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

	Optional<MemberEntity> findByMemberLoginId(String loginId);

}

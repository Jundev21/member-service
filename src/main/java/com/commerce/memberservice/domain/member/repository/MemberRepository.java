package com.commerce.memberservice.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.memberservice.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}

package com.commerce.memberservice.domain.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.commerce.memberservice.common.UserRoles;
import com.commerce.memberservice.domain.member.entity.MemberEntity;

@DataJpaTest
public class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;
	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@BeforeEach
	public void reset() {
		entityManager.flush();
		memberRepository.deleteAll();

	}

	private MemberEntity saveMemberData() {
		return new MemberEntity(
			"홍길동",
			"길동무",
			"gildong123",
			bCryptPasswordEncoder.encode("testT12!"),
			"dongmu@gmail.com",
			"010-1234-1234",
			UserRoles.USER
		);

	}

	@Test
	@DisplayName("회원 등록이 가능하다.")
	public void successRegisterMember() throws Exception {
		//given
		MemberEntity savedMember = memberRepository.save(saveMemberData());
		//when
		//then
		assertThat(savedMember).extracting("memberName", "memberNickName", "memberLoginId", "memberEmail",
				"memberPhoneNumber", "role")
			.containsExactly("홍길동", "길동무", "gildong123", "dongmu@gmail.com", "010-1234-1234", UserRoles.USER);
	}

	@Test
	@DisplayName("회원아이디로 회원 조회가 가능하다.")
	public void successSearchMemberByLoginId() throws Exception {
		//given
		MemberEntity savedMember = memberRepository.save(saveMemberData());
		//when
		Optional<MemberEntity> member = memberRepository.findByMemberLoginId(savedMember.getMemberLoginId());
		//then
		assertThat(member.get()).extracting("memberName", "memberNickName", "memberLoginId", "memberEmail",
				"memberPhoneNumber", "role")
			.containsExactly("홍길동", "길동무", "gildong123", "dongmu@gmail.com", "010-1234-1234", UserRoles.USER);
	}

	@Test
	@DisplayName("회원아이디로 회원 존재 여부 조회가 가능하다.")
	public void successSearchExistMemberByLoginId() throws Exception {
		//given
		MemberEntity savedMember = memberRepository.save(saveMemberData());
		//when
		Boolean existMember = memberRepository.existsByMemberLoginId(savedMember.getMemberLoginId());
		Boolean notExistMember = memberRepository.existsByMemberLoginId("findMember");
		//then
		assertThat(existMember).isEqualTo(true);
		assertThat(notExistMember).isEqualTo(false);
	}

}

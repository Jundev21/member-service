package com.commerce.memberservice.domain.member.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.commerce.memberservice.common.BasicTimeEntity;
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

	private List<MemberEntity> memberList() {
		List<MemberEntity> member = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			member.add(
				new MemberEntity(
					"홍길동" + i,
					"길동무",
					"gildong123",
					"testT12!",
					"dongmu@gmail.com",
					"010-1234-1234",
					UserRoles.USER
				)
			);
		}
		return member;
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

	@Test
	@DisplayName("모든 회원 조회가 가능하다.")
	public void successSearchAllMembers() throws Exception {
		//given
		memberRepository.saveAll(memberList());
		PageRequest pageRequest = PageRequest.of(0, 5);
		//when
		Page<MemberEntity> pageMemberList = memberRepository.findAll(pageRequest);
		//then
		assertThat(pageMemberList.getContent().stream().map(MemberEntity::getMemberName)).usingRecursiveComparison()
			.isEqualTo(memberList().stream().map(MemberEntity::getMemberName).collect(Collectors.toList()));
	}

	@Test
	@DisplayName("가입일 순으로 내림차순 정렬이 가능하다.")
	public void successSearchAllMembersByCreatedDate() throws Exception {
		//given
		List<MemberEntity> savedAllMembers = memberRepository.saveAll(memberList());
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("createdDate").descending());
		List<LocalDateTime> sortedByCreatedDate = savedAllMembers.stream()
			.map(BasicTimeEntity::getCreatedDate).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		//when
		Page<MemberEntity> pageMemberList = memberRepository.findAll(pageRequest);
		//then
		assertThat(pageMemberList.getContent().stream().map(BasicTimeEntity::getCreatedDate)).usingRecursiveComparison()
			.isEqualTo(sortedByCreatedDate);
	}

	@Test
	@DisplayName("회원이름 순으로 오름차순 조회가 가능하다.")
	public void successSearchAllMembersByMemberName() throws Exception {
		//given
		List<MemberEntity> savedAllMembers = memberRepository.saveAll(memberList());
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("memberName"));
		List<String> sortedByMemberName = savedAllMembers.stream()
			.map(MemberEntity::getMemberName).sorted().collect(Collectors.toList());
		//when
		Page<MemberEntity> pageMemberList = memberRepository.findAll(pageRequest);
		//then
		assertThat(pageMemberList.getContent().stream().map(MemberEntity::getMemberName)).usingRecursiveComparison()
			.isEqualTo(sortedByMemberName);
	}
}

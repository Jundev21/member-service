package com.commerce.memberservice.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.commerce.memberservice.common.UserRoles;
import com.commerce.memberservice.common.exception.BasicException;
import com.commerce.memberservice.common.exception.ErrorCode;
import com.commerce.memberservice.domain.member.dto.Request.MemberEditInfoDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.dto.Response.LoginResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberBasicResponse;
import com.commerce.memberservice.domain.member.dto.Response.MemberEditInfoResponseDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberListResponseDto;
import com.commerce.memberservice.domain.member.entity.MemberEntity;
import com.commerce.memberservice.domain.member.repository.MemberRepository;
import com.commerce.memberservice.filter.auth.MemberDetail;
import com.commerce.memberservice.jwt.JwtTokenInfo;

@SpringBootTest
public class MemberServiceTest {
	@Autowired
	private MemberService memberService;
	@MockBean
	private MemberRepository memberRepository;
	@MockBean
	private AuthenticationManager authenticationManager;
	@MockBean
	private JwtTokenInfo jwtTokenInfo;
	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Nested
	@DisplayName("사용자 회원가입 서비스 테스트")
	public class memberRegister {
		@Test
		@DisplayName("회원가입 실패 - 이미 존재하는 멤버")
		public void failMemberRegisterByValidation() throws Exception {
			//given
			MemberRegisterDto memberRegisterDto = MemberRegisterDto.builder()
				.memberName("홍길동")
				.memberNickName("길동무")
				.memberLoginId("gildong123")
				.memberPassword("test123T!!")
				.memberEmail("dongmu@gmail.com")
				.memberPhoneNumber("010-5462-3452")
				.build();
			//when
			when(memberRepository.existsByMemberLoginId(anyString())).thenReturn(true);
			//then
			assertThrows(BasicException.class, () -> memberService.memberRegister(memberRegisterDto));
		}

		@Test
		@DisplayName("회원가입 성공")
		public void successMemberRegister() throws Exception {
			//given
			MemberRegisterDto memberRegisterDto = MemberRegisterDto.builder()
				.memberName("홍길동")
				.memberNickName("길동무")
				.memberLoginId("gildong123")
				.memberPassword("test123T!!")
				.memberEmail("dongmu@gmail.com")
				.memberPhoneNumber("010-5462-3452")
				.build();

			MemberEntity member =
				new MemberEntity(
					"홍길동",
					"길동무",
					"gildong123",
					bCryptPasswordEncoder.encode("testT12!"),
					"dongmu@gmail.com",
					"010-1234-1234",
					UserRoles.USER
				);

			//when
			when(memberRepository.existsByMemberLoginId(anyString())).thenReturn(false);
			when(memberRepository.save(any())).thenReturn(member);
			//then
			assertDoesNotThrow(() -> memberService.memberRegister(memberRegisterDto));
			verify(memberRepository).save(any(MemberEntity.class));
		}
	}

	@Nested
	@DisplayName("사용자 로그인 서비스 테스트")
	public class memberLogin {
		@Test
		@DisplayName("로그인 실패 회원정보 다름")
		public void failMemberLoginByValid() throws Exception {
			//given
			MemberEntity member =
				new MemberEntity(
					"홍길동",
					"길동무",
					"gildong123",
					bCryptPasswordEncoder.encode("testT12!"),
					"dongmu@gmail.com",
					"010-1234-1234",
					UserRoles.USER
				);

			MemberLoginDto memberLoginDto = MemberLoginDto.builder()
				.loginId("gildong1234")
				.password(bCryptPasswordEncoder.encode("testF12!"))
				.build();

			//when
			when(authenticationManager.authenticate(any()))
				.thenThrow(new BadCredentialsException("로그인 정보 오류"));

			// then
			assertThrows(BadCredentialsException.class, () -> {
				memberService.memberLogin(memberLoginDto);
			});

		}

		@Test
		@DisplayName("로그인 성공")
		public void failMemberLoginByPassword() throws Exception {
			//given
			MemberEntity member =
				new MemberEntity(
					"홍길동",
					"길동무",
					"gildong123",
					bCryptPasswordEncoder.encode("testT12!"),
					"dongmu@gmail.com",
					"010-1234-1234",
					UserRoles.USER
				);

			MemberDetail memberDetail = new MemberDetail(member);

			MemberLoginDto memberLoginDto = MemberLoginDto.builder()
				.loginId("gildong123")
				.password(bCryptPasswordEncoder.encode("testT12!"))
				.build();

			Authentication authentication = mock(Authentication.class);

			when(authentication.getPrincipal()).thenReturn(memberDetail);
			when(authenticationManager.authenticate(any())).thenReturn(authentication);
			when(jwtTokenInfo.generateToken(anyString())).thenReturn("generated jwt token");

			// When
			LoginResponseDto responseDto = memberService.memberLogin(memberLoginDto);

			// Then
			assertNotNull(responseDto);
			assertEquals("generated jwt token", responseDto.getJwtToken());
			verify(authenticationManager, times(1)).authenticate(any());
		}
	}

	@Nested
	@DisplayName("사용자 회원정보 수정 서비스 테스트")
	public class memberEditInfo {
		@Test
		@DisplayName("회원정보 수정 실패 유효하지않은 회원")
		@WithMockUser(username = "gildong123", password = "test123!")
		public void failMemberEditInfoByValid() throws Exception {

			//given
			MemberEntity member =
				new MemberEntity(
					"홍길동",
					"길동무",
					"gildong123",
					bCryptPasswordEncoder.encode("testT12!"),
					"dongmu@gmail.com",
					"010-1234-1234",
					UserRoles.USER
				);

			MemberEditInfoDto memberEditInfoDto =
				MemberEditInfoDto.builder()
					.memberName("홍길동수정")
					.memberNickName("길동무수정")
					.memberPassword("test123123!")
					.memberEmail("edited@gmail.com")
					.memberPhoneNumber("010-1234-1234")
					.build();

			//when
			when(memberRepository.findByMemberLoginId(anyString())).thenThrow(
				new BasicException(ErrorCode.NOT_EXIST_MEMBER, ErrorCode.NOT_EXIST_MEMBER.getMsg())
			);

			// then
			assertThrows(BasicException.class, () -> {
				memberService.memberEditInfo(anyString(), memberEditInfoDto);
			});
		}

		@Test
		@DisplayName("회원정보 수정 성공")
		@WithMockUser(username = "gildong123", password = "test123!")
		public void successMemberEditInfoByValid() throws Exception {
			//given
			MemberEntity member =
				new MemberEntity(
					"홍길동",
					"길동무",
					"gildong123",
					bCryptPasswordEncoder.encode("testT12!"),
					"dongmu@gmail.com",
					"010-1234-1234",
					UserRoles.USER
				);

			MemberEditInfoDto memberEditInfoDto =
				MemberEditInfoDto.builder()
					.memberName("홍길동수정")
					.memberNickName("길동무수정")
					.memberPassword("test123123!")
					.memberEmail("edited@gmail.com")
					.memberPhoneNumber("010-1234-1234")
					.build();

			//when
			when(memberRepository.findByMemberLoginId(anyString())).thenReturn(Optional.of(member));
			MemberEditInfoResponseDto result = memberService.memberEditInfo(anyString(), memberEditInfoDto);

			// then
			assertNotNull(result);
			assertThat(result).extracting("memberName", "memberNickName", "memberLoginId", "memberEmail",
					"memberPhoneNumber")
				.containsExactly("홍길동수정", "길동무수정", "gildong123", "edited@gmail.com", "010-1234-1234");

		}

		@Nested
		@WithMockUser
		@DisplayName("회원정보 조회 테스트")
		public class MemberSearchList {
			@Test
			@DisplayName("회원조회가 가능하다.")
			public void successSearchMemberList() throws Exception {
				//given
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
				Pageable pageable = PageRequest.of(0, 5, Sort.by("memberName").ascending());
				Page<MemberEntity> memberList = new PageImpl<>(member);
				//when
				when(memberRepository.findAll(any(Pageable.class))).thenReturn((memberList));
				MemberListResponseDto result = memberService.memberList(pageable);
				//then
				assertNotNull(result);
				assertThat(
					result.getMemberList().stream().map(MemberBasicResponse::getMemberName)).usingRecursiveComparison()
					.isEqualTo(member.stream().map(MemberEntity::getMemberName).collect(Collectors.toList()));
			}

		}

	}

}
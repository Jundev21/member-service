package com.commerce.memberservice.domain.member.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.commerce.memberservice.common.UserRoles;
import com.commerce.memberservice.domain.member.dto.Request.MemberEditInfoDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.dto.Response.MemberEditInfoResponseDto;
import com.commerce.memberservice.domain.member.entity.MemberEntity;
import com.commerce.memberservice.domain.member.service.MemberService;
import com.commerce.memberservice.filter.auth.MemberDetail;
import com.commerce.memberservice.filter.auth.MemberDetailService;
import com.commerce.memberservice.jwt.JwtTokenInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberDetailService memberDetailService;
	@Autowired
	private JwtTokenInfo jwtTokenInfo;
	@MockBean
	private AuthenticationManager authenticationManager;
	@Autowired
	private WebApplicationContext context;
	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Nested
	@DisplayName("사용자 회원가입 테스트")
	public class memberRegister {
		@Test
		@DisplayName("회원가입 실패 유효성검사 (빈값,이메일,핸드폰번호)")
		public void failMemberRegisterByValidation() throws Exception {
			MemberRegisterDto memberRegisterDto = MemberRegisterDto.builder()
				.memberName("")
				.memberNickName("길동무")
				.memberLoginId("gildong123")
				.memberPassword("test123!!")
				.memberEmail("dongmugmail.com")
				.memberPhoneNumber("010-54623452")
				.build();

			mockMvc.perform(post("/api/user/join")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(memberRegisterDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.memberEmail").value("Email 형식이 아닙니다."))
				.andExpect(jsonPath("$.memberPhoneNumber").value("핸드폰 번호 형식이 아닙니다."))
				.andExpect(jsonPath("$.memberName").value("이름은 필수 입력 값입니다."));
		}

		@Test
		@DisplayName("회원가입 성공")
		public void successMemberRegister() throws Exception {
			MemberRegisterDto memberRegisterDto = MemberRegisterDto.builder()
				.memberName("홍길동")
				.memberNickName("길동무")
				.memberLoginId("gildong123")
				.memberPassword("test123!!")
				.memberEmail("dongmu@gmail.com")
				.memberPhoneNumber("010-5462-3452")
				.build();

			mockMvc.perform(post("/api/user/join")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(memberRegisterDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.statusCode").value(201))
				.andExpect(jsonPath("$.message").value("요청에 성공하였습니다. Created"));
		}
	}

	@Nested
	@DisplayName("사용자 로그인 테스트")
	public class memberLogin {
		@Test
		@DisplayName("로그인 실패 유효성검사 (빈값)")
		public void failMemberLoginByValid() throws Exception {
			MemberLoginDto memberLoginInfo = MemberLoginDto.builder()
				.loginId("")
				.password("")
				.build();

			mockMvc.perform(post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(memberLoginInfo)))
				.andExpect(jsonPath("$.loginId").value("아이디는 필수 값 입니다."))
				.andExpect(jsonPath("$.password").value("비밀번호는 필수 값 입니다."));
		}

		@Test
		@WithMockUser(username = "gildong123", password = "testT123!")
		@DisplayName("로그인 성공")
		public void failMemberLoginByPassword() throws Exception {
			MemberLoginDto memberLoginInfo = MemberLoginDto.builder()
				.loginId("gildong123")
				.password("testT123!")
				.build();

			mockMvc.perform(post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(memberLoginInfo)))
				.andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("사용자 회원정보 수정 테스트")
	public class memberEditInfo {
		@Test
		@DisplayName("회원정보 수정 실패 유효성검사")
		@WithMockUser(username = "gildong123", password = "test123!")
		public void failMemberEditInfoByValid() throws Exception {
			MemberEditInfoDto memberEditInfoDto = MemberEditInfoDto.builder()
				.memberName("홍길동Updated")
				.memberNickName("길동무Updated")
				.memberPassword("test1")
				.memberEmail("Updatedgmail.com")
				.memberPhoneNumber("0100000-0000")
				.build();

			mockMvc.perform(put("/api/user/gildong123")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(memberEditInfoDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.memberPhoneNumber").value("핸드폰 번호 형식이 아닙니다."))
				.andExpect(jsonPath("$.memberPassword").value(
					"비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다."))
				.andExpect(jsonPath("$.memberEmail").value("Email 형식이 아닙니다."));

		}

		@Test
		@DisplayName("회원정보 수정 성공")
		// @WithMockUser(username = "gildong123", password = "testT12!")
		public void successMemberEditInfoByValid() throws Exception {
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
			MemberEditInfoDto memberEditInfoDto = MemberEditInfoDto.builder()
				.memberName("홍길동")
				.memberNickName("길동무")
				.memberPassword("testT12!")
				.memberEmail("teste12@gmail.com")
				.memberPhoneNumber("010-0000-0000")
				.build();

			MemberEditInfoResponseDto memberEditInfoResponseDto = MemberEditInfoResponseDto.builder()
				.memberName("홍길동Updated")
				.memberNickName("길동무Updated")
				.memberLoginId("gildong123")
				.memberEmail("Updated@gmail.com")
				.memberPhoneNumber("010-0000-0000")
				.build();



			MemberDetail memberDetail = new MemberDetail(member);

			MemberLoginDto memberLoginDto = MemberLoginDto.builder()
				.loginId("gildong123")
				.password(bCryptPasswordEncoder.encode("testT12!"))
				.build();

			Authentication authentication = mock(Authentication.class);

			when(authentication.getPrincipal()).thenReturn(memberDetail);
			when(authenticationManager.authenticate(any())).thenReturn(authentication);



			when(memberService.memberEditInfo(anyString(), any())).thenReturn(memberEditInfoResponseDto);

			mockMvc.perform(put("/api/user/gildong123")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(memberEditInfoDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseData.memberName").value("홍길동Updated"))
				.andExpect(jsonPath("$.responseData.memberNickName").value("길동무Updated"))
				.andExpect(jsonPath("$.responseData.memberLoginId").value("gildong123"))
				.andExpect(jsonPath("$.responseData.memberEmail").value("Updated@gmail.com"))
				.andExpect(jsonPath("$.responseData.memberPhoneNumber").value("010-0000-0000"));

		}
	}
}
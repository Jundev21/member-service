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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.commerce.memberservice.domain.member.dto.Request.MemberLoginDto;
import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.service.MemberService;
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
	private JwtTokenInfo jwtTokenInfo;
	@Autowired
	AuthenticationManager authenticationManager;

	@Nested
	@DisplayName("사용자 회원가입 테스트")
	class memberRegister {
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
	class memberLogin {
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
		@DisplayName("로그인 실패 존재하지 않는 회원")
		public void failMemberLoginByNotFoundMember() throws Exception {
			MemberLoginDto memberLoginInfo = MemberLoginDto.builder()
				.loginId("test")
				.password("test123!")
				.build();

			mockMvc.perform(post("/api/user/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(memberLoginInfo)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
		}

		// @Test
		// @WithMockUser
		// @DisplayName("로그인 성공시 토큰발행")
		// public void failMemberLoginByPassword() throws Exception {
		// 	MemberLoginDto memberLoginInfo = MemberLoginDto.builder()
		// 		.loginId("test")
		// 		.password("test123!")
		// 		.build();
		//
		// 	Authentication authentication = mock(Authentication.class);
		// 	given(authenticationManager.authenticate(
		// 		new UsernamePasswordAuthenticationToken(
		// 			"test",
		// 			"test123!"
		// 		)
		// 	)).willReturn((Authentication)memberLoginInfo);
		// 	given(authentication.getPrincipal()).willReturn(memberLoginInfo);
		// 	given(jwtTokenInfo.generateToken(anyString())).willReturn(anyString());
		//
		// 	mockMvc.perform(post("/api/user/login")
		// 			.contentType(MediaType.APPLICATION_JSON)
		// 			.content(objectMapper.writeValueAsString(memberLoginInfo)))
		// 		.andExpect(status().isOk());
		// }
	}

}
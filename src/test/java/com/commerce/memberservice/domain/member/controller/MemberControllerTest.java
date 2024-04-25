package com.commerce.memberservice.domain.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.commerce.memberservice.domain.member.dto.Request.MemberRegisterDto;
import com.commerce.memberservice.domain.member.service.MemberService;
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

	@Nested
	@DisplayName("사용자 회원가입 테스")
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

}
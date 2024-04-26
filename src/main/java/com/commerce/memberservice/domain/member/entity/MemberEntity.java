package com.commerce.memberservice.domain.member.entity;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.commerce.memberservice.common.BasicTimeEntity;
import com.commerce.memberservice.common.UserRoles;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "memberTable")
public class MemberEntity extends BasicTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long memberId;
	@Column(name = "name")
	private String memberName;
	@Column(name = "nickname")
	private String memberNickName;
	@Column(name = "loginId")
	private String memberLoginId;
	@Column(name = "password")
	private String memberPassword;
	@Column(name = "email")
	private String memberEmail;
	@Column(name = "phoneNumber")
	private String memberPhoneNumber;
	@Column(name = "role")
	private UserRoles role = UserRoles.USER;

	public MemberEntity(String memberName, String memberNickName, String memberLoginId,
		String memberPassword, String memberEmail, String memberPhoneNumber, UserRoles role) {
		this.memberName = memberName;
		this.memberNickName = memberNickName;
		this.memberLoginId = memberLoginId;
		this.memberPassword = memberPassword;
		this.memberEmail = memberEmail;
		this.memberPhoneNumber = memberPhoneNumber;
		this.role = role;
	}

}

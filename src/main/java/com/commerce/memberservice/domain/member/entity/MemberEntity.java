package com.commerce.memberservice.domain.member.entity;

import java.sql.Array;
import java.util.ArrayList;
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
public class MemberEntity extends BasicTimeEntity implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long memberId;
	@Column(name = "name")
	private String memberName;
	@Column(name = "loginId")
	private String memberLoginId;
	@Column(name = "password")
	private String memberPassword;
	@Column(name = "email")
	private String memberEmail;
	@Column(name = "phoneNumber")
	private String memberPhoneNumber;
	@Column(name = "age")
	private String memberAge;
	@Column(name = "role")
	private UserRoles role = UserRoles.USER;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(this.getRole().toString()));
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}

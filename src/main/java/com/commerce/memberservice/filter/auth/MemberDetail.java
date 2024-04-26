package com.commerce.memberservice.filter.auth;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.commerce.memberservice.domain.member.entity.MemberEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberDetail implements UserDetails {

	private final MemberEntity memberEntity;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(this.memberEntity.getRole().toString()));
	}
	@Override
	public String getPassword() {
		return memberEntity.getMemberPassword();
	}
	@Override
	public String getUsername() {
		return this.memberEntity.getMemberLoginId();
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
}

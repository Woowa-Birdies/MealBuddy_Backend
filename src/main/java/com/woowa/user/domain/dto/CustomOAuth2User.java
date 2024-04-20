package com.woowa.user.domain.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User, UserDetails {

	private final UserDTO userDTO;

	public CustomOAuth2User(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add((GrantedAuthority)userDTO::role);

		return collection;
	}

	@Override
	public String getPassword() {
		return null;
	}

	// TODO: 닉네임을 유니크로 할 것인지 정해야 함 / 유니크 아니라면 UserID로 반환하는 것이 맞는 것 같음
	@Override
	public String getUsername() {
		return userDTO.name();
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

	@Override
	public String getName() {
		return userDTO.name();
	}

	public Long getUserId() {
		return userDTO.userId();
	}

	public String getExternalID() {
		return userDTO.externalId();
	}
}

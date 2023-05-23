package com.hodolog.api.config;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {

	private final Long userId;

	public UserPrincipal(com.hodolog.api.domain.User user) {
		super(user.getEmail(), user.getPassword(),
			List.of(
				new SimpleGrantedAuthority("ROLE_ADMIN"),
				new SimpleGrantedAuthority("WRITE")));
		this.userId = user.getId();
	}

	public Long getUserId() {
		return userId;
	}
}

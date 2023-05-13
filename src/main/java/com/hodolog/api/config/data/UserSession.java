package com.hodolog.api.config.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSession {

	private Long id;

	public UserSession(Long id) {
		this.id = id;
	}
}

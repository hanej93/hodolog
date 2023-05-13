package com.hodolog.api.exception;

import org.springframework.http.HttpStatus;

public class InvalidSignInInformation extends HodologException {

	private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

	public InvalidSignInInformation() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}
}

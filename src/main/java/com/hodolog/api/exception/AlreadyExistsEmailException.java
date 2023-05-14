package com.hodolog.api.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsEmailException extends HodologException {

	public static String MESSAGE = "이미 가입된 이메일입니다.";

	public AlreadyExistsEmailException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}
}

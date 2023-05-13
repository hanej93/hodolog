package com.hodolog.api.exception;

import org.springframework.http.HttpStatus;

public class Unauthorized extends HodologException {

    private static String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}

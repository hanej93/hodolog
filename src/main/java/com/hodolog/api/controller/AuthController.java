package com.hodolog.api.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.request.Login;
import com.hodolog.api.response.SessionResponse;
import com.hodolog.api.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth/login")
	public SessionResponse login(@RequestBody @Valid Login login) {
		String accessToken = authService.signIn(login);

		return new SessionResponse(accessToken);
	}
}

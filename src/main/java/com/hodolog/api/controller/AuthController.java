package com.hodolog.api.controller;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.config.AppConfig;
import com.hodolog.api.request.Login;
import com.hodolog.api.response.SessionResponse;
import com.hodolog.api.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final AppConfig appConfig;

	@PostMapping("/auth/login")
	public SessionResponse login(@RequestBody @Valid Login login) {
		Long userId = authService.signIn(login);

		Key key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

		String jws = Jwts.builder()
			.setSubject(String.valueOf(userId))
			.signWith(key)
			.setIssuedAt(new Date())
			.compact();

		return new SessionResponse(jws);
	}
}

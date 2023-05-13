package com.hodolog.api.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.domain.User;
import com.hodolog.api.exception.InvalidRequest;
import com.hodolog.api.exception.InvalidSignInInformation;
import com.hodolog.api.request.Login;
import com.hodolog.api.respository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;

	@PostMapping("/auth/login")
	public void login(@RequestBody @Valid Login login) {
		// json 아이디/비밀번호
		log.info("login >> {}", login);

		// DB에서 조회
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
			.orElseThrow(InvalidSignInInformation::new);

		// 토근을 응답

	}
}

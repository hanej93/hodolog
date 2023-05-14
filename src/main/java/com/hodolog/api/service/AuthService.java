package com.hodolog.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.Session;
import com.hodolog.api.domain.User;
import com.hodolog.api.exception.InvalidSignInInformation;
import com.hodolog.api.request.Login;
import com.hodolog.api.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;

	@Transactional
	public Long signIn(Login login) {
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
			.orElseThrow(InvalidSignInInformation::new);
		Session session = user.addSession();

		return user.getId();
	}
}

package com.hodolog.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.User;
import com.hodolog.api.exception.AlreadyExistsEmailException;
import com.hodolog.api.request.Signup;
import com.hodolog.api.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent((user) -> {
				throw new AlreadyExistsEmailException();
			});

		String encryptedPassword = passwordEncoder.encode(signup.getPassword());

		User user = User.builder()
			.email(signup.getEmail())
			.password(encryptedPassword)
			.name(signup.getName())
			.build();
		userRepository.save(user);
	}

}

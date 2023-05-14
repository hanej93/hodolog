package com.hodolog.api.service;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.Session;
import com.hodolog.api.domain.User;
import com.hodolog.api.exception.AlreadyExistsEmailException;
import com.hodolog.api.exception.InvalidRequest;
import com.hodolog.api.exception.InvalidSignInInformation;
import com.hodolog.api.request.Login;
import com.hodolog.api.request.Signup;
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

	@Transactional
	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent((user) -> {
				throw new AlreadyExistsEmailException();
			});

		SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);
		String encryptedPassword = sCryptPasswordEncoder.encode(signup.getPassword());

		User user = User.builder()
			.email(signup.getEmail())
			.password(encryptedPassword)
			.name(signup.getName())
			.build();
		userRepository.save(user);
	}

}

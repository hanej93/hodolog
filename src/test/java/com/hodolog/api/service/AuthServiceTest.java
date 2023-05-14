package com.hodolog.api.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hodolog.api.domain.User;
import com.hodolog.api.exception.AlreadyExistsEmailException;
import com.hodolog.api.request.Login;
import com.hodolog.api.request.Signup;
import com.hodolog.api.respository.UserRepository;

@SpringBootTest
class AuthServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

	@AfterEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원가입 성공")
	void test1() throws Exception {
		// given
		Signup signup = Signup.builder()
			.email("hodolman@gamil.com")
			.password("1234")
			.name("호돌맨")
			.build();

		// when
		authService.signup(signup);

		// then
		assertThat(userRepository.count()).isEqualTo(1L);

		User user = userRepository.findAll().iterator().next();
		assertThat(user.getEmail()).isEqualTo("hodolman@gamil.com");
		assertThat(user.getPassword()).isNotEqualTo("1234").isNotNull();
		assertThat(user.getName()).isEqualTo("호돌맨");
	}

	@Test
	@DisplayName("회원가입시 중복된 이메일")
	void test2() throws Exception {
		// given
		User user = User.builder()
			.email("hodolman@gmail.com")
			.password("1234")
			.name("짱돌맨")
			.build();
		userRepository.save(user);

		Signup signup = Signup.builder()
			.email("hodolman@gmail.com")
			.password("1234")
			.name("호돌맨")
			.build();

		// expected
		assertThatThrownBy(() -> authService.signup(signup))
			.isInstanceOf(AlreadyExistsEmailException.class)
			.hasMessage("이미 가입된 이메일입니다.");
	}

}
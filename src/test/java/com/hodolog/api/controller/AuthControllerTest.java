package com.hodolog.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.api.config.AppConfig;
import com.hodolog.api.domain.Session;
import com.hodolog.api.domain.User;
import com.hodolog.api.request.Login;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.respository.SessionRepository;
import com.hodolog.api.respository.UserRepository;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("로그인 성공")
	void test() throws Exception {
		// given
		User user = User.builder()
			.name("호돌맨")
			.email("hodolman@gmail.com")
			.password("1234")
			.build();
		userRepository.save(user);

		Login login = Login.builder()
			.email("hodolman@gmail.com")
			.password("1234")
			.build();

		String request = objectMapper.writeValueAsString(login);

		// expected
		mockMvc.perform(post("/auth/login")
				.contentType(APPLICATION_JSON)
				.content(request)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@Transactional
	@DisplayName("로그인 성공 후 세션 1개 생성")
	void test2() throws Exception {
		// given
		User user = User.builder()
			.name("호돌맨")
			.email("hodolman@gmail.com")
			.password("1234")
			.build();
		userRepository.save(user);

		Login login = Login.builder()
			.email("hodolman@gmail.com")
			.password("1234")
			.build();

		String request = objectMapper.writeValueAsString(login);

		// expected
		mockMvc.perform(post("/auth/login")
				.contentType(APPLICATION_JSON)
				.content(request)
			)
			.andExpect(status().isOk())
			.andDo(print());

		assertThat(user.getSessions().size()).isEqualTo(1L);
	}

	@Test
	@Transactional
	@DisplayName("로그인 성공 후 세션 응답")
	void test3() throws Exception {
		// given
		User user = User.builder()
			.name("호돌맨")
			.email("hodolman@gmail.com")
			.password("1234")
			.build();
		userRepository.save(user);

		Login login = Login.builder()
			.email("hodolman@gmail.com")
			.password("1234")
			.build();

		String request = objectMapper.writeValueAsString(login);

		// expected
		mockMvc.perform(post("/auth/login")
				.contentType(APPLICATION_JSON)
				.content(request)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists())
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
	void test4() throws Exception {
		// given
		User user = User.builder()
			.name("호돌맨")
			.email("hodolman@gmail.com")
			.password("1234")
			.build();
		Session session = user.addSession();
		userRepository.save(user);

		// expected
		mockMvc.perform(get("/foo")
				.header("authorization", session.getAccessToken())
				.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
	void test5() throws Exception {
		// given
		User user = User.builder()
			.name("호돌맨")
			.email("hodolman@gmail.com")
			.password("1234")
			.build();
		Session session = user.addSession();
		userRepository.save(user);

		// expected
		mockMvc.perform(get("/foo")
				.header("authorization", session.getAccessToken() + "-other")
				.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isUnauthorized())
			.andDo(print());
	}

}
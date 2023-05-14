package com.hodolog.api.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.api.config.AppConfig;
import com.hodolog.api.domain.User;
import com.hodolog.api.request.Signup;
import com.hodolog.api.respository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@ActiveProfiles("test")
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

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원 가입")
	void test6() throws Exception {
		// given
		Signup signup = Signup.builder()
			.email("hodolman@gamil.com")
			.password("1234")
			.name("호돌맨")
			.build();


		// expected
		mockMvc.perform(post("/auth/signup")
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signup))
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

}
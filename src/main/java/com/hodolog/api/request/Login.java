package com.hodolog.api.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;

@Data
public class Login {

	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
}

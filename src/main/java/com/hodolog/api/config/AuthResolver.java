package com.hodolog.api.config;

import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.hodolog.api.config.data.UserSession;
import com.hodolog.api.exception.Unauthorized;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String accessToken = webRequest.getHeader("Authorization");

		if (ObjectUtils.isEmpty(accessToken)) {
			throw new Unauthorized();
		}
		// 데이터베이스 사용자 확인 작업

		return new UserSession(1L);
	}
}

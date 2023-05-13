package com.hodolog.api.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.hodolog.api.config.data.UserSession;
import com.hodolog.api.domain.Session;
import com.hodolog.api.exception.Unauthorized;
import com.hodolog.api.respository.SessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

	private final SessionRepository sessionRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		if (servletRequest == null) {
			log.error("servletRequest Null");
			throw new Unauthorized();
		}

		Cookie[] cookies = servletRequest.getCookies();
		if(cookies.length == 0) {
			log.error("쿠키가 없음");
			throw new Unauthorized();
		}

		String accessToken = null;

		for (Cookie cookie : cookies) {
			if ("SESSION".equals(cookie.getName())) {
				accessToken = cookie.getValue();
				break;
			}
		}

		if (ObjectUtils.isEmpty(accessToken)) {
			throw new Unauthorized();
		}

		Session session = sessionRepository.findByAccessToken(accessToken)
			.orElseThrow(Unauthorized::new);

		return new UserSession(session.getUser().getId());
	}
}

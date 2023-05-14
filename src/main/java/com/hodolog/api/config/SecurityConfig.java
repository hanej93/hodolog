package com.hodolog.api.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.hodolog.api.domain.User;
import com.hodolog.api.respository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return web -> web.ignoring()
			.requestMatchers("*.ico", "/error")
			.requestMatchers(toH2Console());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeRequests()
				.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
				.requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
				.anyRequest().permitAll()
			// .authenticated()
			.and()
			.formLogin()
				.loginPage("/auth/login")
				.loginProcessingUrl("/auth/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/")
				.permitAll()
			.and()
			.rememberMe()
				.rememberMeParameter("remember")
				.alwaysRemember(false)
				.tokenValiditySeconds(3600 * 24 * 30)
			.and()
			.csrf(AbstractHttpConfigurer::disable)
			.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
			return new UserPrincipal(user);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SCryptPasswordEncoder(16, 8, 1, 32, 64);
	}

}

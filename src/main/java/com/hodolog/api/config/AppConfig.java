package com.hodolog.api.config;

import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "hodolman")
public class AppConfig {

	private byte[] jwtKey;

	public void setJwtKey(String jwtKey) {
		this.jwtKey = Base64.getDecoder().decode(jwtKey);
	}

	public byte[] getJwtKey() {
		return jwtKey;
	}
}

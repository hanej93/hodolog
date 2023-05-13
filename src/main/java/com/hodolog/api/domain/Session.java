package com.hodolog.api.domain;

import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String accessToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Session(User user) {
		this.accessToken = UUID.randomUUID().toString();
		this.user = user;
	}
}

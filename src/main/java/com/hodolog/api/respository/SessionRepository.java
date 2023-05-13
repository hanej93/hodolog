package com.hodolog.api.respository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.hodolog.api.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {

	Optional<Session> findByAccessToken(String accessToken);
}

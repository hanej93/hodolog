package com.hodolog.api.respository;

import org.springframework.data.repository.CrudRepository;

import com.hodolog.api.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {

}

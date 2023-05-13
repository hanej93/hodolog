package com.hodolog.api.respository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.hodolog.api.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmailAndPassword(String email, String password);
}

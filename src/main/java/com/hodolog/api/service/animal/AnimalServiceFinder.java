package com.hodolog.api.service.animal;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnimalServiceFinder {

	private final List<AnimalService> animalServices;

	public AnimalService find(String type) {
		return animalServices.stream()
					.filter(service -> service.getType().name().equals(type))
					.findAny()
					.orElseThrow(RuntimeException::new);
	}
}

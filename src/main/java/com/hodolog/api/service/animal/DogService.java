package com.hodolog.api.service.animal;

import org.springframework.stereotype.Component;

import com.hodolog.api.domain.AnimalType;

@Component
public class DogService implements AnimalService {

	@Override
	public String getSound() {
		return "멍멍";
	}

	@Override
	public AnimalType getType() {
		return AnimalType.DOG;
	}
}

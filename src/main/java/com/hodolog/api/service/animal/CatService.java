package com.hodolog.api.service.animal;

import org.springframework.stereotype.Component;

import com.hodolog.api.domain.AnimalType;

@Component
public class CatService implements AnimalService {

	@Override
	public String getSound() {
		return "야옹";
	}

	@Override
	public AnimalType getType() {
		return AnimalType.CAT;
	}
}

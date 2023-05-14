package com.hodolog.api.domain;

import java.lang.reflect.InvocationTargetException;

import com.hodolog.api.service.animal.AnimalService;
import com.hodolog.api.service.animal.CatService;
import com.hodolog.api.service.animal.DogService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnimalType {
	CAT(CatService.class),
	DOG(DogService.class);

	private final Class<? extends AnimalService> animalService;

	public AnimalService create() {
		try {
			return animalService.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}

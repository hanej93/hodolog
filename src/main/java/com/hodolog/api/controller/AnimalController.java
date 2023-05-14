package com.hodolog.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.domain.AnimalType;
import com.hodolog.api.service.animal.AnimalService;
import com.hodolog.api.service.animal.AnimalServiceFinder;
import com.hodolog.api.service.animal.CatService;
import com.hodolog.api.service.animal.DogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AnimalController {

	private final AnimalServiceFinder animalServiceFinder;

	private final Map<String, AnimalService> animalServiceMap;

	@GetMapping("/sound")
	public String sound(@RequestParam String type) {
		AnimalService animalService = animalServiceFinder.find(type);
		return animalService.getSound();
	}

	@GetMapping("/sound/v2")
	public String soundV2(@RequestParam String type) {
		AnimalService animalService = animalServiceMap.get(type.toLowerCase() + "Service");
		return animalService.getSound();
	}

	@GetMapping("/sound/v3")
	public String soundV3(@RequestParam String type) {
		AnimalType animalType = AnimalType.valueOf(type);
		AnimalService animalService = animalType.create();
		return animalService.getSound();
	}

}

package com.github.mmakart.testTaskDeeplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.github.mmakart.testTaskDeeplay.util.Parser;

public class App {
	public static void main(String[] args) {
		
		String propertySetFilename = args[0];
		String criteriaFilename = args[1];
		String animalsFilename = args[2];
		
		try (Scanner propertySetInput = new Scanner(Path.of(propertySetFilename),
					StandardCharsets.UTF_8);
			Scanner criteriaListsInput = new Scanner(Path.of(criteriaFilename),
					StandardCharsets.UTF_8);
			Scanner animalsInput = new Scanner(Path.of(animalsFilename),
					StandardCharsets.UTF_8);
			BufferedReader criteriaLines = Files.newBufferedReader(Path.of(criteriaFilename),
					StandardCharsets.UTF_8);) {
			
			Parser parser = new Parser(
					Parser.parseAvailablePropertyValues(propertySetInput));
			
			List<List<Criteria>> criteriaLists =
					parser.parseCriteriaLists(criteriaListsInput);
			
			List<Animal> animals = parser.parseAnimals(animalsInput);
			
			Map<String, Integer> result = countByCriteria(criteriaLines, criteriaLists, animals);
			
			for (var entry : result.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Map<String, Integer> countByCriteria(
			BufferedReader criteriaLines, List<List<Criteria>> criteriaLists,
			List<Animal> animals) throws IOException {
		Map<String, Integer> result = new LinkedHashMap<>();
		
		String criteriaLine;
		var iterator = criteriaLists.iterator();
		while ((criteriaLine = criteriaLines.readLine()) != null) {
			int counter = 0;
			List<Criteria> criteriaList = iterator.next();
			for (Animal animal : animals) {
				if (isAnimalMatchingAnyOfCriteria(criteriaList, animal)) {
					counter++;
				}
			}
			result.put(criteriaLine, counter);
		}
		
		return result;
	}

	public static boolean isAnimalMatchingAnyOfCriteria(
			List<Criteria> criteriaList, Animal animal) {
		
		boolean result = false;
		
		for (Criteria criteria : criteriaList) {
			result |= criteria.animalMatches(animal);
		}
		
		return result;
	}

}

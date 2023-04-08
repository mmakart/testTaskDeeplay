package testTaskDeeplay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.mmakart.testTaskDeeplay.Animal;
import com.github.mmakart.testTaskDeeplay.App;
import com.github.mmakart.testTaskDeeplay.Criteria;
import com.github.mmakart.testTaskDeeplay.util.Parser;

public class ParserTests {
	private static Parser parser;
	
	@BeforeAll
	public static void init() throws IOException {
		Scanner in = new Scanner(new StringReader(
				"weight:light,average,heavy\n"
				+ "height:low,average,tall\n"
				+ "dietType:herbivore,carnivore,omnivore"));
		parser = new Parser(Parser.parseAvailablePropertyValues(in));
	}
	
	@Test
	public void parse5AnimalsTest() throws IOException {
		Scanner animalsIn = new Scanner(new StringReader(
				"average,tall,omnivore\n"
				+ "average,tall,omnivore\n"
				+ "average,tall,omnivore\n"
				+ "average,tall,omnivore\n"
				+ "average,tall,omnivore"));
		List<Animal> animals = parser.parseAnimals(animalsIn);
		assertEquals(5, animals.size());
	}
	
	@Test
	public void parse3HerbivoreAnimalsTest() throws IOException {
		String animalsString =
				  "average,tall,carnivore\n"
				+ "average,tall,carnivore\n"
				+ "average,tall,herbivore\n"
				+ "average,tall,herbivore\n"
				+ "average,tall,herbivore\n"
				+ "average,tall,omnivore\n";
		
		String criteriaString = "dietType.herbivore";
		
		Map<String, Integer> result = processInput(animalsString, criteriaString);
		
		int expected = 3;
		int real = result.get(criteriaString);
		
		assertEquals(expected, real);
	}
	
	@Test
	public void parse3HeavyAnimalsTest() throws IOException {
		String animalsString =
				  "light,tall,carnivore\n"
				+ "average,tall,carnivore\n"
				+ "average,tall,herbivore\n"
				+ "heavy,tall,herbivore\n"
				+ "heavy,tall,herbivore\n"
				+ "heavy,tall,omnivore";
		
		String criteriaString = "weight.heavy";
		
		Map<String, Integer> result = processInput(animalsString, criteriaString);
		
		int expected = 3;
		int real = result.get(criteriaString);
		
		assertEquals(expected, real);
	}

	private Map<String, Integer> processInput(String animalsString, String criteriaString) throws IOException {
		Scanner animalsIn = new Scanner(new StringReader(animalsString));
		Scanner criteriaIn = new Scanner(new StringReader(criteriaString));
		BufferedReader linesIn = new BufferedReader(new StringReader(criteriaString));
		
		List<Animal> animals = parser.parseAnimals(animalsIn);
		List<List<Criteria>> criteriaLists = parser.parseCriteriaLists(criteriaIn);
		
		Map<String, Integer> result =
				App.countByCriteria(linesIn, criteriaLists, animals);
		return result;
	}
	
	@Test
	public void parse5TallAnimalsTest() throws IOException {
		String animalsString =
				  "light,tall,carnivore\n"
				+ "average,tall,carnivore\n"
				+ "average,tall,herbivore\n"
				+ "heavy,tall,herbivore\n"
				+ "heavy,low,herbivore\n"
				+ "heavy,tall,omnivore";
		
		String criteriaString = "height.tall";
		
		Map<String, Integer> result = processInput(animalsString, criteriaString);
		
		int expected = 5;
		int real = result.get(criteriaString);
		
		assertEquals(expected, real);
	}
}

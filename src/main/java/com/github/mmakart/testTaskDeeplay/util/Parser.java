package com.github.mmakart.testTaskDeeplay.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.github.mmakart.testTaskDeeplay.Animal;
import com.github.mmakart.testTaskDeeplay.Criteria;

public class Parser {
	private Map<String, Set<String>> availableProperties;
	
	public Parser(Map<String, Set<String>> availableProperties) {
		this.availableProperties = availableProperties;
	}
	
	public static Map<String, Set<String>> parseAvailablePropertyValues(Scanner in)
			throws IOException {
		
		// order is important
		Map<String, Set<String>> result = new LinkedHashMap<>();
		
		while (in.hasNextLine()) {			
			String line = in.nextLine();
			String[] nameAndValues = line.split(":");
			String propertyName = nameAndValues[0];
			String[] allowedValues = nameAndValues[1].split(",");
			result.put(propertyName, Set.of(allowedValues));
		}
		return result;
	}
	
	public List<List<Criteria>> parseCriteriaLists(Scanner in) throws IOException {
		List<List<Criteria>> result = new ArrayList<>();
		
		while (in.hasNextLine()) {
			String line = in.nextLine();
			result.add(parseCriteriaList(line));
		}
		
		return result;
	}
	
	// Parsing rules:
	// expr: disjunction* EOF
	// disjunction: conjunction ('|' conjunction)*
	// conjunction: property ('&' property)*
	// property: '!' STRING | STRING
	
	private List<Criteria> parseCriteriaList(String line) {
		LexemeBuffer lexemes = new LexemeBuffer(parseToLexemes(line));
		
		if (lexemes.next().getType() == LexemeType.EOF) {
			return List.of();
		}
		
		lexemes.back();
		return parseCriteriaDisjunction(lexemes);
	}
	
	private List<Criteria> parseCriteriaDisjunction(LexemeBuffer lexemes) {
		
		List<Criteria> result = new ArrayList<>();
		result.add(parseCriteriaConjunction(lexemes));
		
		while (true) {
			Lexeme lexeme = lexemes.next();
			switch (lexeme.getType()) {
			case OR:
				result.add(parseCriteriaConjunction(lexemes));
				break;
			case EOF:
				lexemes.back();
				return result;
			default:
				throw new InputStringFormatException(
						"Unexpected token: " + lexeme.getValue());
			}
		}
		
	}

	private Criteria parseCriteriaConjunction(LexemeBuffer lexemes) {
		Criteria criteria = parseProperty(lexemes);
		
		while (true) {
			Lexeme lexeme = lexemes.next();
			switch (lexeme.getType()) {
			case AND:
				criteria.retain(parseProperty(lexemes));
				break;
			case OR:
			case EOF:
				lexemes.back();
				return criteria;
			default:
				throw new InputStringFormatException(
						"Unexpected token: " + lexeme.getValue());
			}
		}
	}
	
	private Criteria parseProperty(LexemeBuffer lexemes) {
		Lexeme lexeme = lexemes.next();
		
		switch (lexeme.getType()) { 
		case NOT:
			lexeme = lexemes.next();
			if (lexeme.getType() != LexemeType.STRING) {
				throw new InputStringFormatException(
						"Wrong token after '!': " + lexeme.getValue());
			}
			return parseCriteriaFromStringRemoving(lexeme.getValue());
		case STRING:
			return parseCriteriaFromStringRetaining(lexeme.getValue());
		default:
			throw new InputStringFormatException(
					"Unknown token: " + lexeme.getValue());
		}
	}

	private Criteria parseCriteriaFromStringRetaining(String s) {
		String[] nameAndValue = s.split("\\.");
		String name = nameAndValue[0];
		String value = nameAndValue[1];
		
		Criteria result = new Criteria(availableProperties);
		
		result.retain(name, value);
		
		return result;
	}
	
	private Criteria parseCriteriaFromStringRemoving(String s) {
		String[] nameAndValue = s.split("\\.");
		String name = nameAndValue[0];
		String value = nameAndValue[1];
		
		Criteria result = new Criteria(availableProperties);
		
		result.remove(name, value);
		
		return result;
	}

	private List<Lexeme> parseToLexemes(String text) {
		List<Lexeme> result = new ArrayList<>();
		
		for (int pos = 0; pos < text.length(); ) {
			char c = text.charAt(pos);
			switch (c) {
				case '&' -> {
					result.add(new Lexeme(LexemeType.AND, "&"));
					pos++;
					continue;
				}
				case '|' -> {
					result.add(new Lexeme(LexemeType.OR, "|"));
					pos++;
					continue;
				}
				case '!' -> {
					result.add(new Lexeme(LexemeType.NOT, "!"));
					pos++;
					continue;
				}
				case ' ' -> {
					pos++;
					continue;
				}
				default -> {
					StringBuilder sb = new StringBuilder();
					while (c != ' ') {
						sb.append(c);
						pos++;
						if (pos >= text.length()) {
							break;
						}
						c = text.charAt(pos);
					}
					result.add(new Lexeme(LexemeType.STRING, sb.toString()));
				}
			};
		}
		result.add(new Lexeme(LexemeType.EOF, ""));
		
		return result;
	}
	
	public List<Animal> parseAnimals(Scanner in) throws IOException {
		
		List<Animal> result = new ArrayList<>();
		
		while (in.hasNextLine()) {
			String line = in.nextLine();
			Map<String, String> properties = new HashMap<>();
			String[] propertyValues = line.split(",");
			
			int i = 0;
			Iterator<String> iterator = availableProperties.keySet().iterator();
			for ( ; iterator.hasNext(); i++) {
				String name = iterator.next();
				String value = propertyValues[i];
				
				properties.put(name, value);
			}
			result.add(new Animal(properties));
		}
		
		return result;
	}
}

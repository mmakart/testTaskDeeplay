package com.github.mmakart.testTaskDeeplay;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Criteria {
	private Map<String, Set<String>> criteria;

	public Criteria(String name, Set<String> values) {
		criteria = new LinkedHashMap<>();
		criteria.put(name, values);
	}
	
	public Criteria(Map<String, Set<String>> other) {
		criteria = new LinkedHashMap<>();
		for (var entry : other.entrySet()) {
			Set<String> myValue = new HashSet<>();
			for (String value : entry.getValue()) {
				myValue.add(value);
			}
			criteria.put(entry.getKey(), myValue);
		}
	}
	
	public void retain(String name, Set<String> values) {
		criteria.get(name).retainAll(values);
	}
	
	public void retain(String name, String value) {
		Set<String> set = new HashSet<>();
		set.add(value);
		criteria.get(name).retainAll(set);
	}
	
	public void remove(String name, Set<String> value) {
		criteria.get(name).removeAll(value);
	}
	
	public void remove(String name, String value) { 
		criteria.get(name).remove(value);
	}
	
	public Set<String> getAllowedValuesFor(String property) {
		return criteria.get(property);
	}
	
	public boolean animalMatches(Animal animal) {
		boolean result = true;
		
		for (String property : criteria.keySet()) {
			result &= criteria.get(property).contains(animal.getProperty(property));
		}
		
		return result;
	}

	public void retain(Criteria other) {
		for (var key : criteria.keySet()) {
			criteria.get(key).retainAll(other.criteria.get(key));
		}
	}

	@Override
	public String toString() {
		return "Criteria [" + criteria + "]";
	}
	
}

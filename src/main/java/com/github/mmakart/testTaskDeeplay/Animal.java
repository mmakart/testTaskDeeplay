package com.github.mmakart.testTaskDeeplay;

import java.util.Map;

public class Animal {
	private Map<String, String> properties;
	
	public Animal(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getProperty(String property) {
		return properties.get(property);
	}

	@Override
	public String toString() {
		return "Animal [properties=" + properties + "]";
	}

}

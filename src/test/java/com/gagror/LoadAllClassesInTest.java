package com.gagror;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class LoadAllClassesInTest {

	@Test
	public void loadAllClasses() {
		// Load all classes to ensure that e.g. code coverage tools see them
		final List<String> allClasses = new ArrayList<>();
		final Reflections reflections = new Reflections("com.gagror", new SubTypesScanner(false));
		for(final Class<?> clazz : reflections.getSubTypesOf(Object.class)) {
			allClasses.add(clazz.getName());
		}
		// Print all class names in order
		Collections.sort(allClasses);
		for(final String clazz : allClasses) {
			System.out.println("Found class: " + clazz);
		}
	}
}

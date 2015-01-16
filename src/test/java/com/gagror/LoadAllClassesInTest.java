package com.gagror;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.reflections.Reflections;

public class LoadAllClassesInTest {

	@Test
	public void loadAllClasses() {
		// Load all classes to ensure that e.g. code coverage tools see them
		final List<String> allClasses = new ArrayList<>();
		final Reflections reflections = new Reflections("com.gagror");
		for(final Class<?> clazz : reflections.getSubTypesOf(Object.class)) {
			allClasses.add(clazz.getName());
		}
	}
}

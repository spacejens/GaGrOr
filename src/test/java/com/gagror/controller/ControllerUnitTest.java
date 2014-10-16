package com.gagror.controller;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class ControllerUnitTest {

	private final String name;

	@SuppressWarnings("rawtypes")
	private final Class controller;

	@Test
	public void inheritsFromCommonSuperclass() {
		final Class<AbstractController> expectedSuperclass = AbstractController.class;
		Class<?> clazz = controller;
		while(clazz.getSuperclass() != null) {
			System.out.println(String.format("Class %s inherits from %s", clazz.getCanonicalName(), clazz.getSuperclass().getCanonicalName()));
			clazz = clazz.getSuperclass();
			if(clazz.equals(expectedSuperclass)) {
				return;
			}
		}
		fail(String.format("Class %s does not inherit from %s", name, expectedSuperclass.getCanonicalName()));
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findControllers() {
		final Set<Object[]> controllers = new HashSet<>();
		final Reflections reflections = new Reflections("com.gagror");
		for(final Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
			controllers.add(new Object[]{clazz.getCanonicalName(), clazz});
		}
		return controllers;
	}
}

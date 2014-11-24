package com.gagror;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public abstract class DesignRulesTestSupport {

	@SuppressWarnings("unchecked")
	protected boolean hasAnyAnnotation(final AnnotatedElement annotatedElement, @SuppressWarnings("rawtypes") final Class... annotations) {
		boolean success = false;
		for(final Class<? extends Annotation> annotation : annotations) {
			success |= null != annotatedElement.getAnnotation(annotation);
		}
		return success;
	}

	protected void assertSuperclass(final Class<?> clazz, final Class<?> superclass) {
		assertTrue(String.format("Class %s should have superclass %s", clazz.getCanonicalName(), superclass.getCanonicalName()), superclass.isAssignableFrom(clazz));
	}

	protected static Iterable<Object[]> parameterizeForAnnotation(final Class<? extends Annotation> annotation) {
		final Set<Object[]> parameters = new HashSet<>();
		final Reflections reflections = new Reflections("com.gagror");
		for(final Class<?> clazz : reflections.getTypesAnnotatedWith(annotation)) {
			parameters.add(new Object[]{clazz.getCanonicalName(), clazz});
		}
		return parameters;
	}

	protected static Iterable<Object[]> parameterizeForConcreteSubclasses(final Class<?> parent) {
		final Set<Object[]> parameters = new HashSet<>();
		final Reflections reflections = new Reflections("com.gagror");
		for(final Class<?> clazz : reflections.getSubTypesOf(parent)) {
			// Ignore inner classes for the purposes of design rule tests
			if(! Modifier.isAbstract(clazz.getModifiers()) && ! clazz.isMemberClass()) {
				parameters.add(new Object[]{clazz.getCanonicalName(), clazz});
			}
		}
		return parameters;
	}
}

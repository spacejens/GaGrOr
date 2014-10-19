package com.gagror;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
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

	protected static Iterable<Object[]> parameterizeForAnnotation(final Class<? extends Annotation> annotation) {
		final Set<Object[]> parameters = new HashSet<>();
		final Reflections reflections = new Reflections("com.gagror");
		for(final Class<?> clazz : reflections.getTypesAnnotatedWith(annotation)) {
			parameters.add(new Object[]{clazz.getCanonicalName(), clazz});
		}
		return parameters;
	}
}

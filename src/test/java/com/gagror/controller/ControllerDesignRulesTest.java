package com.gagror.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gagror.DesignRulesTestSupport;

/**
 * These tests verify common design rules for controller classes.
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
@CommonsLog
public class ControllerDesignRulesTest extends DesignRulesTestSupport {

	@SuppressWarnings("unused")
	private final String name;

	private final Class<?> controller;

	@Test
	public void allRequestMappingMethodsHaveSecurityAnnotations() {
		// Check all methods that have a request mapping, even in superclasses
		for(final Method method : controller.getMethods()) {
			if(method.isAnnotationPresent(RequestMapping.class)) {
				if(! hasSecurityAnnotation(method)) {
					fail(String.format("Method %s on class %s should have a security annotation",
							method.getName(), method.getDeclaringClass().getCanonicalName()));
				}
				log.debug(String.format("Method %s of class %s has security annotation",
						method.getName(), method.getDeclaringClass().getCanonicalName()));
			}
		}
	}

	@Test
	public void restrictUseOfModelAttributeMethods() {
		// Disallow model attribute methods (to prevent them from loading for every page), except for a few exceptions
		for(final Method method : controller.getMethods()) {
			if(method.isAnnotationPresent(ModelAttribute.class)) {
				if(! isAllowedModelAttributeMethod(method)) {
					fail(String.format("Method %s on class %s is not allowed to have the @ModelAttribute annotation",
							method.getName(), method.getDeclaringClass().getCanonicalName()));
				}
			}
		}
	}
	private boolean isAllowedModelAttributeMethod(final Method method) {
		return "getCurrentUser".equals(method.getName())
				&& AbstractController.class.equals(method.getDeclaringClass());
	}

	@Test
	public void allModelAttributeMethodsHaveSecurityAnnotations() {
		// Check all methods that are model attributes, even in superclasses
		for(final Method method : controller.getMethods()) {
			if(method.isAnnotationPresent(ModelAttribute.class)) {
				if(! hasSecurityAnnotation(method)) {
					fail(String.format("Method %s on class %s should have a security annotation",
							method.getName(), method.getDeclaringClass().getCanonicalName()));
				}
				log.debug(String.format("Method %s of class %s has security annotation",
						method.getName(), method.getDeclaringClass().getCanonicalName()));
			}
		}
	}

	@Test
	public void noSecurityAnnotationsOnControllerClass() {
		assertFalse("Security annotations on controller classes are disallowed", hasSecurityAnnotation(controller));
	}

	private boolean hasSecurityAnnotation(final AnnotatedElement clazzOrMethod) {
		return hasAnyAnnotation(clazzOrMethod, PreAuthorize.class);
	}

	@Test
	public void inheritsFromCommonSuperclass() {
		assertSuperclass(controller, AbstractController.class);
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findControllers() {
		return parameterizeForAnnotation(Controller.class);
	}
}

package com.gagror.controller;

import static org.junit.Assert.fail;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;

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
public class ControllerDesignRulesTest extends DesignRulesTestSupport {

	private final String name;

	private final Class<?> controller;

	@Test
	public void allRequestMappingMethodsHaveSecurityAnnotations() {
		// If the controller class has a security annotation, it is valid for all methods
		if(hasSecurityAnnotation(controller)) {
			System.out.println(String.format("Class %s has security annotation", name));
			return;
		}
		// Check all methods that have a request mapping, even in superclasses
		for(final Method method : controller.getMethods()) {
			if(method.isAnnotationPresent(RequestMapping.class)) {
				if(! hasSecurityAnnotation(method)) {
					fail(String.format("Method %s on class %s should have a security annotation",
							method.getName(), method.getDeclaringClass().getCanonicalName()));
				}
				System.out.println(String.format("Method %s of class %s has security annotation",
						method.getName(), method.getDeclaringClass().getCanonicalName()));
			}
		}
	}

	@Test
	public void allModelAttributeMethodsHaveSecurityAnnotations() {
		// If the controller class has a security annotation, it is valid for all methods
		if(hasSecurityAnnotation(controller)) {
			System.out.println(String.format("Class %s has security annotation", name));
			return;
		}
		// Check all methods that are model attributes, even in superclasses
		for(final Method method : controller.getMethods()) {
			if(method.isAnnotationPresent(ModelAttribute.class)) {
				if(! hasSecurityAnnotation(method)) {
					fail(String.format("Method %s on class %s should have a security annotation",
							method.getName(), method.getDeclaringClass().getCanonicalName()));
				}
				System.out.println(String.format("Method %s of class %s has security annotation",
						method.getName(), method.getDeclaringClass().getCanonicalName()));
			}
		}
	}

	private boolean hasSecurityAnnotation(final AnnotatedElement clazzOrMethod) {
		return hasAnyAnnotation(clazzOrMethod, PreAuthorize.class);
	}

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
		return parameterizeForAnnotation(Controller.class);
	}
}

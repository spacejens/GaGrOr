package com.gagror.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.DesignRulesTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class EntityDesignRulesTest extends DesignRulesTestSupport {

	@SuppressWarnings("unused")
	private final String name;

	private final Class<?> entity;

	@Test
	public void hasNoArgsConstructor() throws Exception {
		final Constructor<?> noArgsConstructor = entity.getConstructor();
		assertFalse("No args constructor should not be private", Modifier.isPrivate(noArgsConstructor.getModifiers()));
	}

	@Test
	public void joinColumnNullability() {
		for(final Field field : entity.getDeclaredFields()) {
			if(field.isAnnotationPresent(JoinColumn.class)) {
				if(field.isAnnotationPresent(ManyToOne.class)) {
					assertEquals(String.format("Field %s had @ManyToOne that didn't match the @JoinColumn", field.getName()),
							field.getAnnotation(JoinColumn.class).nullable(),
							field.getAnnotation(ManyToOne.class).optional());
				} else {
					fail(String.format("Field %s has @JoinColumn but no known join annotation", field.getName()));
				}
			}
		}
	}

	@Test
	public void identifiable() {
		assertTrue("All entities should extend Identifiable", Identifiable.class.isAssignableFrom(entity));
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findEntities() {
		return parameterizeForAnnotation(Entity.class);
	}
}

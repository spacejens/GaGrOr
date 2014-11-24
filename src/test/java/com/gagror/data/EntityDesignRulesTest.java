package com.gagror.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.RequiredArgsConstructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.DesignRulesTestSupport;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class EntityDesignRulesTest extends DesignRulesTestSupport {

	private static final Map<String, String> tableToName = new HashMap<>();

	private final String name;

	private final Class<?> entity;

	@Test
	public void hasNoArgsConstructor() throws Exception {
		final Constructor<?> noArgsConstructor = entity.getConstructor();
		assertFalse("No args constructor should not be private", Modifier.isPrivate(noArgsConstructor.getModifiers()));
	}

	@Test(expected=NoSuchMethodException.class)
	public void doesntOverrideEquals() throws Exception {
		entity.getDeclaredMethod("equals", Object.class);
	}

	@Test(expected=NoSuchMethodException.class)
	public void doesntOverrideHashCode() throws Exception {
		entity.getDeclaredMethod("hashCode");
	}

	@Test
	public void overridesToString() throws Exception {
		entity.getDeclaredMethod("toString");
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
	public void oneToOneParentChildRelationship() {
		for(final Field field : entity.getDeclaredFields()) {
			if(field.isAnnotationPresent(OneToOne.class)) {
				final OneToOne annotation = field.getAnnotation(OneToOne.class);
				if(null != annotation.mappedBy() && ! annotation.mappedBy().isEmpty()) {
					// This annotation is the parent, proceed to find the child
					final Class<?> childEntity = field.getType();
					boolean foundChildField = false;
					for(final Field childField : childEntity.getDeclaredFields()) {
						if(childField.getName().equals(annotation.mappedBy())) {
							foundChildField = true;
							assertTrue(String.format("Child field %s.%s should have reverse mapping", childEntity, childField.getName()),
									childField.isAnnotationPresent(OneToOne.class));
							assertFalse(String.format("Child field %s.%s should not be optional", childEntity, childField.getName()),
									childField.getAnnotation(OneToOne.class).optional());
						}
					}
					assertTrue(String.format("Failed to find child field of %s.%s", entity, field.getName()),
							foundChildField);
				}
			}
		}
	}

	@Test
	public void identifiable() {
		assertTrue("All entities should extend Identifiable", Identifiable.class.isAssignableFrom(entity));
	}

	@Test
	public void explicitTableAnnotation() {
		assertTrue("All entities should have @Table", entity.isAnnotationPresent(Table.class));
		final String tableName = entity.getAnnotation(Table.class).name();
		assertFalse(String.format("Table name \"%s\" also used by %s", tableName, tableToName.get(tableName)), tableToName.containsKey(tableName));
		tableToName.put(tableName, name);
	}

	@Test
	public void inheritsFromCommonSuperclass() {
		assertSuperclass(entity, AbstractEntity.class);
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findEntities() {
		return parameterizeForAnnotation(Entity.class);
	}
}

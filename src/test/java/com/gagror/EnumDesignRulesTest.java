package com.gagror;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Properties;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gagror.data.Identifiable;
import com.gagror.data.PropertyNameDisplayable;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
@CommonsLog
public class EnumDesignRulesTest extends DesignRulesTestSupport {

	private static final Properties displayNameProperties = new Properties();

	private final String name;

	private final Class<?> clazz;

	@Test
	public void identifiableEnumsHaveReverseLookup() throws Exception {
		if(! Identifiable.class.isAssignableFrom(clazz)) {
			log.debug(String.format("Skipping reverse lookup for non-identifiable enum %s", name));
			return;
		}
		log.debug(String.format("Verifying reverse lookup for %s", name));
		final Method method = clazz.getMethod("fromId", Integer.class);
		assertTrue("Reverse lookup should be static", Modifier.isStatic(method.getModifiers()));
		assertEquals("Reverse lookup should return enum class", clazz, method.getReturnType());
	}

	@Test
	public void allDisplayNamePropertiesDefined() throws Exception {
		if(! PropertyNameDisplayable.class.isAssignableFrom(clazz)) {
			log.debug(String.format("Skipping display name property check for non-displayable enum %s", name));
			return;
		}
		for(final Object instance : clazz.getEnumConstants()) {
			log.debug(String.format("Verifying display name property for enum %s.%s", name, instance));
			final Method method = clazz.getMethod("getDisplayNameProperty");
			assertEquals("Wrong return type", String.class, method.getReturnType());
			final String key = (String)method.invoke(instance);
			assertTrue(String.format("Property %s should be defined in enums.properties", instance),
					displayNameProperties.containsKey(key));
			assertFalse(String.format("Property %s should not be empty in enums.properties", instance),
					displayNameProperties.get(key).toString().isEmpty());
		}
	}

	@BeforeClass
	public static void readDisplayNameProperties() throws Exception {
		final File file = new File("src/main/resources/messages/enums.properties");
		displayNameProperties.load(new FileInputStream(file));
	}

	@Parameters(name="{0}")
	public static Iterable<Object[]> findEnums() {
		return parameterizeForConcreteSubclasses(Enum.class);
	}
}

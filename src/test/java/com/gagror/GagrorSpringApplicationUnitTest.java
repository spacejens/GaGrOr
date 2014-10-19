package com.gagror;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.MessageSource;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import com.gagror.service.accesscontrol.GagrorPermissionEvaluator;
import com.google.common.io.PatternFilenameFilter;

@RunWith(MockitoJUnitRunner.class)
public class GagrorSpringApplicationUnitTest {

	GagrorSpringApplication instance;

	@Mock
	SpringApplicationBuilder application;

	@Test
	public void configure() {
		instance.configure(application);
		verify(application).sources(GagrorSpringApplication.class);
		verifyNoMoreInteractions(application);
	}

	@Test
	public void requestLoggingFilter() {
		final Filter filter = instance.requestLoggingFilter();
		assertTrue("Created filter was not a request logging filter", filter instanceof AbstractRequestLoggingFilter);
	}

	@Test
	public void permissionEvaluator() {
		final PermissionEvaluator evaluator = instance.permissionEvaluator();
		assertEquals("Unexpected class of permission evaluator", GagrorPermissionEvaluator.class, evaluator.getClass());
	}

	@Test
	public void messageSource() throws Exception {
		final MessageSource source = instance.messageSource();
		final Object[] noArgs = new Object[]{};
		int countResolved = 0;
		// Verify that all property files in the messages directory can be resolved
		final File directory = new File("src/main/resources/messages");
		for(final File propertyFile : directory.listFiles(new PatternFilenameFilter(".*\\.properties"))) {
			// Load the property file
			final Properties properties = new Properties();
			properties.load(new FileInputStream(propertyFile));
			// Find a property to print
			propertykey: for(final Object key : properties.keySet()) {
				final String expected = properties.get(key).toString();
				// Verify that the property does not require parameters
				if(! expected.contains("{")) {
					final String resolved = source.getMessage(key.toString(), noArgs, Locale.getDefault());
					countResolved++;
					System.out.println(String.format("Resolving property %s from file %s", key, propertyFile.getName()));
					assertEquals(String.format("Property %s from file %s resolved to wrong message", key, propertyFile.getName()), expected, resolved);
					// Resolve and verify the result
					break propertykey;
				}
			}
		}
		assertNotEquals("Should have resolved at least one property", 0, countResolved);
	}

	@Before
	public void createInstance() {
		instance = new GagrorSpringApplication();
	}
}

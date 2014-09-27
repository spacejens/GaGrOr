package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public class SessionCredentialsComponentUnitTest {

	@Test
	public void scope() {
		final Scope scope = SessionCredentialsComponent.class.getAnnotation(Scope.class);
		assertNotNull("Missing scope annotation", scope);
		assertEquals("Wrong scope value", "session", scope.value());
		assertEquals("Wrong scope proxy mode", ScopedProxyMode.TARGET_CLASS, scope.proxyMode());
	}
}

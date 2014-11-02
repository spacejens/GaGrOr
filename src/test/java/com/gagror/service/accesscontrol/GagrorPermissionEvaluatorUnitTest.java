package com.gagror.service.accesscontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import lombok.extern.apachecommons.CommonsLog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.reflections.Reflections;
import org.springframework.security.core.Authentication;

import com.gagror.data.account.AccountEntity;

@RunWith(MockitoJUnitRunner.class)
@CommonsLog
public class GagrorPermissionEvaluatorUnitTest {

	private static final String PERMISSION_NAME = "testPermission";
	private static final String ANOTHER_PERMISSION_NAME = "anotherTestPermission";
	private static final String NOT_FOUND_PERMISSION_NAME = "notFoundPermission";

	GagrorPermissionEvaluator instance;

	GagrorPermissionEvaluator instanceDefault;

	@Mock
	GagrorPermission permission;

	@Mock
	GagrorPermission anotherPermission;

	@Mock
	Authentication authentication;

	@Mock
	AccessControlService accessControlService;

	@Mock
	AccountEntity accountEntity;

	@Test
	public void allPermissionsAddedByDefault() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final Reflections reflections = new Reflections("com.gagror");
		int count = 0;
		for(final Class<? extends GagrorPermission> clazz : reflections.getSubTypesOf(GagrorPermission.class)) {
			if(! Modifier.isAbstract(clazz.getModifiers()) && ! Modifier.isPrivate(clazz.getModifiers())) {
				log.debug(String.format("Verifying that permission has been added: %s", clazz.getCanonicalName()));
				count++;
				final GagrorPermission expected = clazz.getConstructor().newInstance();
				final GagrorPermission actual = instanceDefault.getPermission(expected.getName());
				assertNotNull(String.format("Permission missing for %s", clazz.getCanonicalName()), actual);
				assertSame("Wrong class of permission", expected.getClass(), actual.getClass());
			}
		}
		assertNotEquals("At least one permission should have been added", 0, count);
	}

	@Test(expected=IllegalArgumentException.class)
	public void permissionsAddedByDefaultPreventsNull() {
		instanceDefault.editAccount = null;
		instanceDefault.getPermission("any");
	}

	@Test
	public void hasPermission_domainObject_ok() {
		hasPermission_domainObject(true, true, PERMISSION_NAME);
	}

	@Test
	public void hasPermission_domainObject_notOk() {
		hasPermission_domainObject(false, false, PERMISSION_NAME);
	}

	@Test
	public void hasPermission_domainObject_permissionNotFound() {
		hasPermission_domainObject(true, false, NOT_FOUND_PERMISSION_NAME);
	}

	private void hasPermission_domainObject(final boolean permissionResult, final boolean expectedResult, final String permissionName) {
		final String targetDomainObject = "DomainObject";
		when(permission.hasPermission(accountEntity, targetDomainObject)).thenReturn(permissionResult);
		assertEquals("Unexpected permission check result", expectedResult, instance.hasPermission(authentication, targetDomainObject, permissionName));
	}

	@Test
	public void hasPermission_idType_ok() {
		hasPermission_idType(true, true, PERMISSION_NAME);
	}

	@Test
	public void hasPermission_idType_notOk() {
		hasPermission_idType(false, false, PERMISSION_NAME);
	}

	@Test
	public void hasPermission_idType_permissionNotFound() {
		hasPermission_idType(true, false, NOT_FOUND_PERMISSION_NAME);
	}

	private void hasPermission_idType(final boolean permissionResult, final boolean expectedResult, final String permissionName) {
		final String targetId = "TargetID";
		final String targetType = "TargetType";
		when(permission.hasPermission(accountEntity, targetId, targetType)).thenReturn(permissionResult);
		assertEquals("Unexpected permission check result", expectedResult, instance.hasPermission(authentication, targetId, targetType, permissionName));
	}

	@Test(expected=IllegalStateException.class)
	public void cannotAddDuplicatePermissions() {
		GagrorPermission duplicate = mock(GagrorPermission.class);
		when(duplicate.getName()).thenReturn(PERMISSION_NAME);
		instance.addPermission(duplicate);
	}

	@Before
	public void setupAccessControlService() {
		when(accessControlService.loadAccountEntity(authentication)).thenReturn(accountEntity);
	}

	@Before
	public void setupInstance() {
		when(permission.getName()).thenReturn(PERMISSION_NAME);
		when(anotherPermission.getName()).thenReturn(ANOTHER_PERMISSION_NAME);
		instance = new GagrorPermissionEvaluator(new HashMap<String, GagrorPermission>());
		instance.accessControlService = accessControlService;
		instance.addPermission(permission);
		instance.addPermission(anotherPermission);
	}

	@Before
	public void setupInstanceDefault() {
		instanceDefault = new GagrorPermissionEvaluator();
		instanceDefault.accessControlService = accessControlService;
		// Fill in what would normally be autowired here. The instance will then use these permissions.
		instanceDefault.editAccount = new PermissionEditAccount();
		instanceDefault.hasContact = new PermissionHasContact();
		instanceDefault.hasContactRequest = new PermissionHasContactRequest();
		instanceDefault.viewGroup = new PermissionViewGroup();
	}
}

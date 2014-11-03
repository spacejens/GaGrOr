package com.gagror.service.accesscontrol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
@CommonsLog
@Transactional
public class GagrorPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	AccessControlService accessControlService;

	private final Map<String, GagrorPermission> permissions;

	@Autowired
	PermissionEditAccount editAccount;

	@Autowired
	PermissionHasContact hasContact;

	@Autowired
	PermissionHasContactRequest hasContactRequest;

	@Autowired
	PermissionViewGroup viewGroup;

	@Autowired
	PermissionAdminGroup adminGroup;

	public GagrorPermissionEvaluator() {
		this(new HashMap<String, GagrorPermission>());
	}

	private void initializePermissions() {
		addPermission(editAccount);
		addPermission(hasContact);
		addPermission(hasContactRequest);
		addPermission(viewGroup);
		addPermission(adminGroup);
	}

	@Override
	public boolean hasPermission(
			final Authentication authentication,
			final Object targetDomainObject,
			final Object permission) {
		final GagrorPermission perm = getPermission(permission);
		if(null != perm) {
			log.debug(String.format("Evaluating permission %s for domain object %s", permission, targetDomainObject));
			return perm.hasPermission(
					accessControlService.loadAccountEntity(authentication),
					targetDomainObject);
		} else {
			log.error(String.format("Unknown permission: %s", permission));
			return false;
		}
	}

	@Override
	public boolean hasPermission(
			final Authentication authentication,
			final Serializable targetId,
			final String targetType,
			final Object permission) {
		final GagrorPermission perm = getPermission(permission);
		if(null != perm) {
			log.debug(String.format("Evaluating permission %s for target ID %s of type %s", permission, targetId, targetType));
			return perm.hasPermission(
					accessControlService.loadAccountEntity(authentication),
					targetId,
					targetType);
		} else {
			log.error(String.format("Unknown permission: %s", permission));
			return false;
		}
	}

	protected void addPermission(final GagrorPermission permission) {
		if(null == permission) {
			throw new IllegalArgumentException("Cannot add null permission");
		}
		if(permissions.containsKey(permission.getName())) {
			throw new IllegalStateException(String.format("Cannot add duplicate permission definitions: %s", permission.getName()));
		}
		log.debug(String.format("Adding evaluator for permission %s", permission.getName()));
		permissions.put(permission.getName(), permission);
	}

	protected GagrorPermission getPermission(final Object permission) {
		if(permissions.isEmpty()) {
			initializePermissions();
		}
		return permissions.get(permission);
	}
}

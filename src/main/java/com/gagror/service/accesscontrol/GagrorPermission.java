package com.gagror.service.accesscontrol;

import java.io.Serializable;

import com.gagror.data.account.AccountEntity;

public interface GagrorPermission {

	String getName();

	boolean hasPermission(final AccountEntity account, final Object targetDomainObject);

	boolean hasPermission(final AccountEntity account, final Serializable targetId, final String targetType);
}

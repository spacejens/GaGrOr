package com.gagror.service.accesscontrol;

import com.gagror.data.Identifiable;

public abstract class AbstractGagrorPermissionLongId<E extends Identifiable<Long>> extends AbstractGagrorPermission<Long, E> {

	protected AbstractGagrorPermissionLongId(final String name, final Class<E> targetClass) {
		super(name, targetClass);
	}

	@Override
	protected final Long parseId(final String rawId) {
		return Long.parseLong(rawId);
	}
}

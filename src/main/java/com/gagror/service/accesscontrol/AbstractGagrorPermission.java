package com.gagror.service.accesscontrol;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.gagror.data.account.AccountEntity;

@Data
@RequiredArgsConstructor
// TODO Get ID class from target class generic ID type
public abstract class AbstractGagrorPermission<I> implements GagrorPermission {

	private final String name;

	private final String targetType;

	protected AbstractGagrorPermission(final String name, final Class<?> targetClass) {
		this(name, targetClass.getCanonicalName());
	}

	@Override
	public final boolean hasPermission(final AccountEntity account, final Object targetDomainObject) {
		return hasPermission(
				parseId(targetDomainObject.toString()),
				account);
	}

	@Override
	public final boolean hasPermission(final AccountEntity account, final Serializable targetId, final String targetType) {
		if(! this.targetType.equals(targetType)) {
			throw new IllegalArgumentException(String.format("Permission %s does not support target type %s", name, targetType));
		}
		return hasPermission(
				parseId(targetId.toString()),
				account);
	}

	protected abstract I parseId(final String rawId);

	protected abstract boolean hasPermission(final I id, final AccountEntity account);
}

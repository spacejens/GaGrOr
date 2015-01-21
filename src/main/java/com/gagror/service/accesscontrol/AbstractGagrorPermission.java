package com.gagror.service.accesscontrol;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.gagror.CodingErrorException;
import com.gagror.data.Identifiable;
import com.gagror.data.account.AccountEntity;

@RequiredArgsConstructor
public abstract class AbstractGagrorPermission<I extends Serializable, E extends Identifiable<I>> implements GagrorPermission {

	@Getter
	private final String name;

	@Getter
	private final String targetType;

	protected AbstractGagrorPermission(final String name, final Class<E> targetClass) {
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
			throw new CodingErrorException(String.format("Permission %s does not support target type %s", name, targetType));
		}
		return hasPermission(
				parseId(targetId.toString()),
				account);
	}

	protected abstract I parseId(final String rawId);

	protected abstract boolean hasPermission(final I id, final AccountEntity account);
}

package com.gagror.service;

import com.gagror.data.AbstractEntity;
import com.gagror.data.IdentifiablePersistent;
import com.gagror.data.Input;

public abstract class AbstractIdentifiablePersister<I extends Input & IdentifiablePersistent, E extends AbstractEntity, C extends AbstractEntity>
extends AbstractPersister<I, E, C> {

	@Override
	protected boolean isCreateNew(final I form) {
		return !form.isPersistent();
	}
}

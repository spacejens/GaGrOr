package com.gagror.service;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.validation.BindingResult;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.AbstractEntity;
import com.gagror.data.VersionedInput;

@CommonsLog
public abstract class AbstractIdentifiablePersister<I extends VersionedInput, E extends AbstractEditableEntity, C extends AbstractEntity>
extends AbstractPersister<I, E, C> {

	@Override
	protected boolean isCreateNew(final I form) {
		return !form.isPersistent();
	}

	@Override
	protected void validateSimultaneousEdit(final I form, final BindingResult bindingResult, final E entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit %s failed, simultaneous edit detected", form));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}
}

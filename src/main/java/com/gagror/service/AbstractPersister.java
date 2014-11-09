package com.gagror.service;

import javax.transaction.Transactional;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.validation.BindingResult;

import com.gagror.data.AbstractEntity;

@Transactional
@CommonsLog
public abstract class AbstractPersister<I, E extends AbstractEntity> {

	public final boolean save(final I form, final BindingResult bindingResult) {
		// Check for input errors
		validateForm(form, bindingResult);
		if(bindingResult.hasErrors()) {
			log.warn(String.format("Failed to persist, form had errors: %s", form));
			return false;
		}
		// Update and persist the entity
		final E entity = createOrLoad(form);
		updateValues(form, entity);
		if(! entity.isPersistent()) {
			makePersistent(entity);
		}
		return true;
	}

	protected abstract void validateForm(final I form, final BindingResult bindingResult);

	protected abstract E createOrLoad(final I form);

	protected abstract void updateValues(final I form, final E entity);

	protected abstract void makePersistent(final E entity);
}

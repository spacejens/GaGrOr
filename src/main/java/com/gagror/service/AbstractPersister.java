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
		final E entity = createOrLoad(form); // TODO Persister workflow should distinguish between create and load (e.g. only verify vs existing state if needed)
		validateFormVsExistingState(form, bindingResult, entity);
		if(bindingResult.hasErrors()) {
			log.warn(String.format("Failed to persist, form had errors: %s", form));
			return false;
		}
		// Update and persist the entity
		updateValues(form, entity);
		if(! entity.isPersistent()) {
			makePersistent(entity);
		}
		// Optionally update application state before reporting success
		updateApplicationState(entity);
		return true;
	}

	protected abstract void validateForm(final I form, final BindingResult bindingResult);

	protected void validateFormVsExistingState(final I form, final BindingResult bindingResult, final E entity) {
		// TODO Validation vs existing state should be unsupported operation by default (when only called for already persistent entities)
		// Override this method to add validations specific to the existing entity state
	}

	protected abstract E createOrLoad(final I form);

	protected abstract void updateValues(final I form, final E entity);

	protected void makePersistent(final E entity) {
		throw new UnsupportedOperationException("This persister cannot make new entities persistent");
	}

	protected void updateApplicationState(final E entity) {
		// Override this method to update application state after persisting
	}
}

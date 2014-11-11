package com.gagror.service;

import javax.transaction.Transactional;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.validation.BindingResult;

import com.gagror.data.AbstractEntity;

@Transactional
@CommonsLog
public abstract class AbstractPersister<I, E extends AbstractEntity> {

	public final boolean save(final I form, final BindingResult bindingResult) {
		E entity = null;
		// Check for input errors
		validateForm(form, bindingResult);
		if(! isCreateNew(form)) {
			entity = loadExisting(form);
			if(null == entity) {
				throw new IllegalStateException(String.format("Failed to load existing entity when saving: %s", form));
			}
			validateFormVsExistingState(form, bindingResult, entity);
		}
		if(bindingResult.hasErrors()) {
			log.warn(String.format("Failed to persist, form had errors: %s", form));
			return false;
		}
		// Update and persist the entity
		if(isCreateNew(form)) {
			entity = createNew(form);
			if(null == entity) {
				throw new IllegalStateException(String.format("Failed to create new entity when saving: %s", form));
			}
		}
		updateValues(form, entity);
		if(! entity.isPersistent()) {
			entity = makePersistent(entity);
		}
		postPersistenceUpdate(entity);
		return true;
	}

	protected abstract void validateForm(final I form, final BindingResult bindingResult);

	protected abstract boolean isCreateNew(final I form);

	protected E loadExisting(final I form) {
		throw new UnsupportedOperationException("This persister cannot load existing entities");
	}

	protected void validateFormVsExistingState(final I form, final BindingResult bindingResult, final E entity) {
		// Override this method to add form verifications depending on the existing entity state
	}

	protected E createNew(final I form) {
		throw new UnsupportedOperationException("This persister cannot create new entities");
	}

	protected abstract void updateValues(final I form, final E entity);

	protected E makePersistent(final E entity) {
		throw new UnsupportedOperationException("This persister cannot make new entities persistent");
	}

	protected void postPersistenceUpdate(final E entity) {
		// Override this method to update application state after persisting
	}
}

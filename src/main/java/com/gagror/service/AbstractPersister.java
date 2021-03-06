package com.gagror.service;

import javax.transaction.Transactional;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.validation.BindingResult;

import com.gagror.CodingErrorException;
import com.gagror.data.AbstractEntity;
import com.gagror.data.Input;

@Transactional
@CommonsLog
public abstract class AbstractPersister<I extends Input, E extends AbstractEntity, C extends AbstractEntity> {

	public boolean save(final I form, final BindingResult bindingResult) {
		E entity = null;
		// Check for input errors
		validateForm(form, bindingResult);
		final C context = loadContext(form);
		validateFormVsContext(form, bindingResult, context);
		if(! isCreateNew(form)) {
			entity = loadExisting(form, context);
			if(null == entity) {
				throw new CodingErrorException(String.format("Failed to load existing entity when saving: %s", form));
			}
			validateSimultaneousEdit(form, bindingResult, entity);
			validateFormVsExistingState(form, bindingResult, entity);
		}
		if(bindingResult.hasErrors()) {
			log.warn(String.format("Failed to persist, form had errors: %s", form));
			return false;
		}
		// Update and persist the entity
		if(isCreateNew(form)) {
			entity = createNew(form, context);
			if(null == entity) {
				throw new CodingErrorException(String.format("Failed to create new entity when saving: %s", form));
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

	protected C loadContext(final I form) {
		// Override this method to load context for the persisted entity
		return null;
	}

	protected void validateFormVsContext(final I form, final BindingResult bindingResult, final C context) {
		// OVerride this method to add form validations depending on the context
	}

	protected abstract boolean isCreateNew(final I form);

	protected E loadExisting(final I form, final C context) {
		throw new UnsupportedOperationException("This persister cannot load existing entities");
	}

	protected void validateSimultaneousEdit(final I form, final BindingResult bindingResult, final E entity) {
		// Override this method when doing a simultaneous edit check
	}

	protected void validateFormVsExistingState(final I form, final BindingResult bindingResult, final E entity) {
		// Override this method to add form verifications depending on the existing entity state
	}

	protected E createNew(final I form, final C context) {
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

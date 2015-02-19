package com.gagror.data;

import org.springframework.validation.BindingResult;

public interface VersionedInput extends Input, Versioned, IdentifiablePersistent {

	void addErrorSimultaneuosEdit(final BindingResult bindingResult);
}

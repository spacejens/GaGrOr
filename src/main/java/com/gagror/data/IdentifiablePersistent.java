package com.gagror.data;

public interface IdentifiablePersistent extends Identifiable<Long> {

	boolean isPersistent();

	boolean hasId(final Long id);
}

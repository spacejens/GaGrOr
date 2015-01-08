package com.gagror.data;

public abstract class AbstractInput {

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	// TODO Refactor existing subclasses to use the appropriate abstract subclasses
}

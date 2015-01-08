package com.gagror.data;

public abstract class AbstractInput {

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	// TODO Define abstract input subclasses for shared code, and refactor existing subclasses to use them
}

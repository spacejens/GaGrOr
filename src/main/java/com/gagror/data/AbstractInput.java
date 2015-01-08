package com.gagror.data;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractInput {

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}

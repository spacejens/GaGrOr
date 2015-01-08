package com.gagror.data;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractIdentifiableInput extends AbstractInput {

	@Getter
	@Setter
	private Long id;

	@Override
	public String toString() {
		return String.format("%s, id=%d", super.toString(), id);
	}
}

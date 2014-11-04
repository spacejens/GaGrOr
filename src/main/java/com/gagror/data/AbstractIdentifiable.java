package com.gagror.data;

public abstract class AbstractIdentifiable
implements Identifiable<Long> {

	@Override
	public final boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(null == other
				|| ! this.getClass().equals(other.getClass())
				|| null == getId()) {
			return false;
		}
		final AbstractIdentifiable castOther = (AbstractIdentifiable)other;
		return getId().equals(castOther.getId());
	}

	@Override
	public final int hashCode() {
		if(null == getId()) {
			return 0;
		} else {
			return getId().hashCode();
		}
	}
}

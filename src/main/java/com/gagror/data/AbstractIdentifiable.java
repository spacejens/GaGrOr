package com.gagror.data;

public abstract class AbstractIdentifiable
implements IdentifiablePersistent {

	@Override
	public final boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(null == other
				|| ! this.getClass().equals(other.getClass())
				|| ! isPersistent()) {
			return false;
		}
		final AbstractIdentifiable castOther = (AbstractIdentifiable)other;
		return hasId(castOther.getId());
	}

	@Override
	public final int hashCode() {
		if(! isPersistent()) {
			return 0;
		} else {
			return getId().hashCode();
		}
	}

	@Override
	public final boolean hasId(final Long id) {
		if(isPersistent()) {
			return getId().equals(id);
		} else {
			return false;
		}
	}

	@Override
	public final boolean isPersistent() {
		return getId() != null;
	}

	@Override
	public String toString() {
		return String.format("%s, id=%d", getClass().getSimpleName(), getId());
	}
}

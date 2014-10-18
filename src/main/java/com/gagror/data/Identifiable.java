package com.gagror.data;

import java.io.Serializable;

public interface Identifiable<I extends Serializable> {

	I getId();
}

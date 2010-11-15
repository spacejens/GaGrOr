package se.spacejens.gagror.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import se.spacejens.gagror.LogAwareSupport;

/**
 * Implementation of naming context provider, using the same naming context
 * instance for all calls to {@link #getContext()}.
 * 
 * @author spacejens
 */
public class NamingContextProviderImpl extends LogAwareSupport implements
		NamingContextProvider {

	/** Context instance. */
	private static Context context = null;

	@Override
	public Context getContext() throws NamingException {
		if (null == NamingContextProviderImpl.context) {
			NamingContextProviderImpl.context = new InitialContext();
		}
		return NamingContextProviderImpl.context;
	}
}

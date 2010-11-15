package se.spacejens.gagror.controller;

import javax.naming.Context;
import javax.naming.NamingException;

/**
 * A provider of contexts used for naming (e.g. JNDI) lookups.
 * 
 * @author spacejens
 */
public interface NamingContextProvider {

	/**
	 * Get a naming context to use for lookups.
	 * 
	 * @return Not null.
	 * @throws NamingException
	 *             If naming context creation failed.
	 */
	Context getContext() throws NamingException;
}

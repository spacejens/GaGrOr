package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.Message;

/**
 * Interface for the EJB, used for initial testing purposes.
 * 
 * @author spacejens
 */
public interface MessageService {

	/**
	 * Create a persistent message in the database.
	 * 
	 * @param rc
	 *            The request context.
	 * @param text
	 *            The contents of the message.
	 * @return The detached message object.
	 * @throws GagrorException
	 *             If failed.
	 */
	public Message createMessage(final RequestContext rc, final String text)
			throws GagrorException;
}

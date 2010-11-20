package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
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
	 * @throws ServiceCommunicationException
	 *             If communication with the service failed.
	 * @throws LoginFailedException
	 *             If login using the credentials in the request context fails.
	 */
	public Message createMessage(final RequestContext rc, final String text) throws ServiceCommunicationException, LoginFailedException;
}

package se.spacejens.gagror.controller.ejb;

import javax.ejb.Singleton;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.Message;

/**
 * Implementation of the message service, used for testing.
 * 
 * @author spacejens
 */
@Singleton
public class MessageBean extends EJBSupport implements MessageService {

	@Override
	public Message createMessage(final RequestContext rc, final String text)
			throws GagrorException {
		this.getLog().debug("Message bean creates new message");
		// TODO
		return new Message("An EJB generated non-persistent message appears!");
	}
}

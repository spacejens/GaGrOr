package se.spacejens.gagror.controller.ejb;

import javax.ejb.Singleton;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.Message;

/**
 * Implementation of the message service, used for testing.
 * 
 * @author spacejens
 */
@Singleton
public class MessageBean extends EJBSupport implements MessageService {

	@Override
	public Message createMessage(final RequestContext rc, final String text) throws GagrorException {
		this.getLog().debug("Message bean creates new message");
		final JpaContext jpa = this.getJpaContext(rc);
		final Message message = new Message(text);
		jpa.getEntityManager().persist(message);
		return message;
	}
}

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
	public Message createMessage(final RequestContext rc, final String text)
			throws GagrorException {
		this.getLog().debug("Message bean creates new message");
		// TODO
		final JpaContext jpa = this.getJpaContext(rc);
		if (jpa.getEntityManager() == null) {
			return new Message("EJB does not have an entity manager");
		} else {
			return new Message("EJB has an entity manager");
		}
	}
}

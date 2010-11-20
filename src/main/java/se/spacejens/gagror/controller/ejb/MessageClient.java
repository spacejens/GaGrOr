package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.NamingContextProvider;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.model.Message;

/**
 * Message service client.
 * 
 * @author spacejens
 */
public class MessageClient extends EJBClientSupport<MessageService> implements MessageService {

	/**
	 * Create client instance.
	 * 
	 * @param namingContextProvider
	 *            Used for JNDI lookup.
	 */
	public MessageClient(final NamingContextProvider namingContextProvider) {
		super(namingContextProvider);
	}

	@Override
	protected String getBeanName() {
		return MessageBean.class.getSimpleName();
	}

	@Override
	public Message createMessage(final RequestContext rc, final String text) throws ServiceCommunicationException, LoginFailedException {
		return this.getReference().createMessage(rc, text);
	}
}

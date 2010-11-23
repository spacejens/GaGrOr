package se.spacejens.gagror.view.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.model.Message;

/**
 * Spring controller for the welcome page.
 * 
 * @author spacejens
 */
@Controller
@RequestMapping("/welcome")
public class Welcome extends SpringViewSupport {

	@RequestMapping("/initial.html")
	public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) {
		return new Work() {
			@Override
			protected ModelAndView doWork(final RequestContext rc) throws LoginFailedException, ServiceCommunicationException {
				this.getLog().info("Handling request and providing model");
				final Message message = Welcome.this.getMessageService().createMessage(Welcome.this.getContext(request),
						"An EJB generated persistent message appears!");
				final ModelAndView mav = new ModelAndView("welcome");
				mav.getModel().put("brief", message.getText() + " (" + message.getId() + ")");
				return mav;
			}
		}.process(request);
	}
}

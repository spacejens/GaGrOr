package se.spacejens.gagror.view.spring;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.ejb.MessageClient;
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
	public ModelAndView handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) {
		this.getLog().info("Handling request and providing model");
		final Map<String, Object> model = new HashMap<String, Object>();
		try {
			final Message message = new MessageClient(
					this.getNamingContextProvider()).createMessage(
					this.getAnonymousContext(),
					"An EJB generated persistent message appears!");
			model.put("brief", message.getText() + " (" + message.getId() + ")");
		} catch (GagrorException e) {
			model.put("brief", "An error message appears: "
					+ e.getCause().getMessage());
		}
		// final EntityManagerFactory emf = Persistence
		// .createEntityManagerFactory("GagrorPersistenceUnit");
		// final EntityManager em = emf.createEntityManager();
		// final EntityTransaction trans = em.getTransaction();
		// trans.begin();
		// Message mess1 = new Message("A persistent message appears!");
		// em.persist(mess1);
		// model.put("brief", mess1.getText() + " (" + mess1.getId() + ")");
		// trans.commit();
		// em.close();
		// emf.close();
		return new ModelAndView("welcome", "gagror", model);
	}
}

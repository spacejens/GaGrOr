package se.spacejens.gagror.view;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Definitions for views.
 * 
 * @author spacejens
 */
public enum Views {
	PUBLIC_ERROR("error", ViewParameters.PUBLIC_ERROR_HEADLINE, ViewParameters.PUBLIC_ERROR_MESSAGE),
	PUBLIC_INDEX("index", ViewParameters.COMMON_LOGINFORM),
	PUBLIC_LOGIN("login", ViewParameters.COMMON_LOGINFORM, ViewParameters.PUBLIC_LOGIN_HEADLINE, ViewParameters.PUBLIC_LOGIN_MESSAGE),
	PUBLIC_REGISTER("register", ViewParameters.PUBLIC_REGISTER_FORM),
	DASHBOARD_INDEX("dashboard/index");

	/** The view file name, without prefix or suffix. */
	private final String name;

	/** The parameters that need to be set for this view. */
	private final Set<ViewParameters> parameters;

	/**
	 * Create instance.
	 * 
	 * @param name
	 *            View file name without prefix or suffix, not null.
	 * @param parameters
	 *            The parameters for this view.
	 */
	Views(final String name, final ViewParameters... parameters) {
		this.name = name;
		final Set<ViewParameters> tempSet = EnumSet.noneOf(ViewParameters.class);
		for (final ViewParameters param : parameters) {
			tempSet.add(param);
		}
		this.parameters = Collections.unmodifiableSet(tempSet);
	}

	/**
	 * Get the view file name, without prefix or suffix.
	 * 
	 * @return Not null.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the parameters that need to be set for this view.
	 * 
	 * @return Unmodifiable set, not null.
	 */
	public Set<ViewParameters> getParameters() {
		return this.parameters;
	}
}

package com.gagror.controller.wh40kskirmish;

import com.gagror.controller.AbstractController;
import com.gagror.controller.FormAndURLMismatchException;
import com.gagror.data.wh40kskirmish.gangs.GangIdentifiable;

public abstract class AbstractWh40kSkirmishController extends AbstractController {

	protected static final String ATTR_GANGTYPE_ID = "gangTypeId";
	protected static final String ATTR_FACTION_ID = "factionId";
	protected static final String ATTR_GANG_ID = "gangId";
	protected static final String ATTR_FIGHTER_ID = "fighterId";

	protected static final String MAY_PLAY_GANG = IS_LOGGED_IN + " and hasPermission(#" + ATTR_GANG_ID + ", 'wh40kskGangPlayer')";

	protected <F extends GangIdentifiable> void verifyURLGangIdMatchesForm(final Long urlGangId, final F form) {
		if(! urlGangId.equals(form.getGangId())) {
			throw new FormAndURLMismatchException("Gang ID", urlGangId, form.getGangId());
		}
	}
}

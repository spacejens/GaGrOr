package com.gagror.controller.wh40kskirmish;

import javax.validation.Valid;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gagror.controller.AbstractController;
import com.gagror.data.wh40kskirmish.gangs.EditGangOutput;
import com.gagror.data.wh40kskirmish.gangs.GangInput;
import com.gagror.service.wh40kskirmish.gangs.GangPersister;
import com.gagror.service.wh40kskirmish.gangs.GangService;

@Controller
@RequestMapping("/wh40kskirmish/gang")
@CommonsLog
public class GangController extends AbstractController {

	protected static final String ATTR_GANGTYPE_ID = "gangTypeId";
	protected static final String ATTR_FACTION_ID = "factionId";
	protected static final String ATTR_GANG_ID = "gangId";

	@Autowired
	GangService gangService;

	@Autowired
	GangPersister gangPersister;

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/create", method=RequestMethod.GET)
	public String createGangForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create gang form for group %d", groupId));
		final EditGangOutput data = gangService.prepareToCreateGang(groupId);
		model.addAttribute("group", data.getGroup());
		model.addAttribute("factions", data.getFactions());
		model.addAttribute("gangForm", new GangInput(data.getRules()));
		return "wh40kskirmish/gang_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/save", method=RequestMethod.POST)
	public Object saveGangForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("gangForm") final GangInput gangForm,
			final BindingResult bindingResult) {
		verifyURLGroupIdMatchesForm(groupId, gangForm);
		if(gangPersister.save(gangForm, bindingResult)) {
			log.info(String.format("Saved gang: %s", gangForm));
			return redirect(String.format("/wh40kskirmish/group/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", gangForm));
			if(! gangForm.isPersistent()) {
				final EditGangOutput data = gangService.prepareToCreateGang(groupId);
				model.addAttribute("group", data.getGroup());
				model.addAttribute("factions", data.getFactions());
			} else {
				final EditGangOutput data = gangService.prepareToEditGang(
						groupId,
						gangForm.getId());
				model.addAttribute("group", data.getGroup());
			}
			return "wh40kskirmish/gang_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/{" + ATTR_GANG_ID + "}")
	public String viewGang(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANG_ID) final Long gangId,
			final Model model) {
		log.info(String.format("Viewing gang %d in group %d", gangId, groupId));
		model.addAttribute("gang", gangService.viewGang(groupId, gangId));
		// TODO Show fighters on view gang page
		return "wh40kskirmish/gang_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/{" + ATTR_GANG_ID + "}/edit", method=RequestMethod.GET)
	public String editGangForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANG_ID) final Long gangId,
			final Model model) {
		log.info(String.format("Viewing edit gang form for gang %d in group %d", gangId, groupId));
		final EditGangOutput data = gangService.prepareToEditGang(groupId, gangId);
		model.addAttribute("group", data.getGroup());
		model.addAttribute("gangForm", new GangInput(data.getCurrentState()));
		return "wh40kskirmish/gang_edit";
	}

	// TODO Add page to create (buy) or edit fighter (group owner unlocks additional capabilities?)

	// TODO Add page to view fighter
}

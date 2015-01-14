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
import com.gagror.data.wh40kskirmish.Wh40kSkirmishGangTypeInput;
import com.gagror.service.social.GroupService;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishGangTypePersister;
import com.gagror.service.wh40kskirmish.Wh40kSkirmishRulesService;

@Controller
@RequestMapping("/wh40kskirmish/rules")
@CommonsLog
public class Wh40kSkirmishRulesController extends AbstractController {

	protected static final String ATTR_GANGTYPE_ID = "gangTypeId";
	protected static final String ATTR_FACTION_ID = "factionId";

	@Autowired
	GroupService groupService;

	@Autowired
	Wh40kSkirmishRulesService rulesService;

	@Autowired
	Wh40kSkirmishGangTypePersister gangTypePersister;

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}")
	public String viewRules(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing rules for group %d", groupId));
		model.addAttribute("rules", rulesService.viewRules(groupId));
		// TODO Make initial territory allocation for gangs configurable
		// TODO Make initial money per gang configurable
		// TODO Make name of currency in game configurable
		return "wh40kskirmish/rules_view";
	}

	// TODO Add page to edit basic rules

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/create", method=RequestMethod.GET)
	public String createGangTypeForm(@PathVariable(ATTR_GROUP_ID) final Long groupId, final Model model) {
		log.info(String.format("Viewing create gang type form for group %d", groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("gangTypeForm", new Wh40kSkirmishGangTypeInput(groupId));
		return "wh40kskirmish/gangtypes_edit";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/save", method=RequestMethod.POST)
	public Object saveGangTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			final Model model,
			@Valid @ModelAttribute("gangTypeForm") final Wh40kSkirmishGangTypeInput gangTypeForm,
			final BindingResult bindingResult) {
		if(! groupId.equals(gangTypeForm.getGroupId())) {
			log.error(String.format("Group ID URL (%d) and form (%d) mismatch when attempting to save gang type form", groupId, gangTypeForm.getGroupId()));
			throw new IllegalArgumentException(String.format("Unexpected group ID in gang type form"));
		}
		if(gangTypePersister.save(gangTypeForm, bindingResult)) {
			log.info(String.format("Saved gang type: %s", gangTypeForm));
			return redirect(String.format("/wh40kskirmish/rules/%d", groupId));
		} else {
			log.warn(String.format("Failed to save: %s", gangTypeForm));
			model.addAttribute("group", groupService.viewGroup(groupId));
			return "wh40kskirmish/gangtypes_edit";
		}
	}

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/gangtype/{" + ATTR_GANGTYPE_ID + "}")
	public String viewGangType(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("gangType", rulesService.viewGangTypeListChildren(groupId, gangTypeId));
		return "wh40kskirmish/gangtypes_view";
	}

	@PreAuthorize(MAY_ADMIN_GROUP)
	@RequestMapping(value="/{" + ATTR_GROUP_ID + "}/gangtype/{" + ATTR_GANGTYPE_ID + "}/edit", method=RequestMethod.GET)
	public String editGangTypeForm(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			final Model model) {
		log.info(String.format("Viewing edit gang type form for gang type %d in group %d", gangTypeId, groupId));
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("gangTypeForm", new Wh40kSkirmishGangTypeInput(rulesService.viewGangType(groupId, gangTypeId)));
		return "wh40kskirmish/gangtypes_edit";
	}

	// TODO Add page for creating faction of gang type

	@PreAuthorize(MAY_VIEW_GROUP)
	@RequestMapping("/{" + ATTR_GROUP_ID + "}/faction/{" + ATTR_GANGTYPE_ID + "}/{" + ATTR_FACTION_ID + "}")
	public String viewFaction(
			@PathVariable(ATTR_GROUP_ID) final Long groupId,
			@PathVariable(ATTR_GANGTYPE_ID) final Long gangTypeId,
			@PathVariable(ATTR_FACTION_ID) final Long factionId,
			final Model model) {
		model.addAttribute("group", groupService.viewGroup(groupId));
		model.addAttribute("faction", rulesService.viewFaction(groupId, gangTypeId, factionId));
		return "wh40kskirmish/factions_view";
	}

	// TODO Add page for editing faction of gang type

	// TODO Add pages for creating, viewing, and editing races of gang types

	// TODO Add pages for creating, viewing, and editing fighter types of races

	// TODO Add page to view skills

	// TODO Add page to edit skills

	// TODO Add page to view equipment

	// TODO Add page to edit equipment

	// TODO Add page to view territories

	// TODO Add page to edit territories
}

package com.gagror.service.wh40kskirmish;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesInput;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class Wh40kSkirmishRulesPersister
extends AbstractPersister<Wh40kSkirmishRulesInput, Wh40kSkirmishRulesEntity, GroupEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Override
	protected void validateForm(final Wh40kSkirmishRulesInput form, final BindingResult bindingResult) {
		// Nothing to do that isn't verified by annotations already
	}

	@Override
	protected GroupEntity loadContext(final Wh40kSkirmishRulesInput form) {
		final GroupEntity group = groupRepository.findOne(form.getGroupId());
		if(null == group.getWh40kSkirmishRules()) {
			throw new IllegalStateException(String.format("Group %s lacks WH40K skirmish rules", group));
		}
		return group;
	}

	@Override
	protected boolean isCreateNew(final Wh40kSkirmishRulesInput form) {
		return false;
	}

	@Override
	protected Wh40kSkirmishRulesEntity loadExisting(final Wh40kSkirmishRulesInput form, final GroupEntity context) {
		return context.getWh40kSkirmishRules();
	}

	@Override
	protected void validateFormVsExistingState(
			final Wh40kSkirmishRulesInput form,
			final BindingResult bindingResult,
			final Wh40kSkirmishRulesEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit rules %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected void updateValues(final Wh40kSkirmishRulesInput form, final Wh40kSkirmishRulesEntity entity) {
		entity.setStartingMoney(form.getStartingMoney());
		entity.setCurrencyName(form.getCurrencyName());
	}
}

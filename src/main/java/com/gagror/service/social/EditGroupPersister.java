package com.gagror.service.social;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.AbstractEntity;
import com.gagror.data.DataNotFoundException;
import com.gagror.data.group.GroupEditInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.service.AbstractPersister;

@Service
@CommonsLog
public class EditGroupPersister extends AbstractPersister<GroupEditInput, GroupEntity, AbstractEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Override
	protected void validateForm(final GroupEditInput form, final BindingResult bindingResult) {
		if(form.isViewableByAnyone()) {
			for(final GroupEntity groupViewableByAnyone : groupRepository.findByViewableByAnyone(true)) {
				if(groupViewableByAnyone.getName().equals(form.getName())
						&& ! groupViewableByAnyone.getId().equals(form.getId())) {
					form.addErrorNameMustBeUniqueWhenViewableByAnyone(bindingResult);
				}
			}
		}
	}

	@Override
	protected boolean isCreateNew(final GroupEditInput form) {
		return false;
	}

	@Override
	protected GroupEntity loadExisting(final GroupEditInput form, final AbstractEntity context) {
		final GroupEntity group = groupRepository.findOne(form.getId());
		if(null == group) {
			throw new DataNotFoundException(String.format("Group %d", form.getId()));
		}
		return group;
	}

	@Override
	protected void validateFormVsExistingState(final GroupEditInput form, final BindingResult bindingResult, final GroupEntity entity) {
		if(! form.getVersion().equals(entity.getVersion())) {
			log.warn(String.format("Attempt to edit group %d failed, simultaneous edit detected", form.getId()));
			form.addErrorSimultaneuosEdit(bindingResult);
		}
	}

	@Override
	protected void updateValues(final GroupEditInput form, final GroupEntity entity) {
		entity.setName(form.getName());
		entity.setViewableByAnyone(form.isViewableByAnyone());
	}
}

package com.gagror.service.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.gagror.data.AbstractEntity;
import com.gagror.data.group.GroupEditInput;
import com.gagror.data.group.GroupEntity;
import com.gagror.data.group.GroupRepository;
import com.gagror.service.AbstractIdentifiablePersister;

@Service
public class EditGroupPersister extends AbstractIdentifiablePersister<GroupEditInput, GroupEntity, AbstractEntity> {

	@Autowired
	GroupRepository groupRepository;

	@Override
	protected void validateForm(final GroupEditInput form, final BindingResult bindingResult) {
		if(form.isViewableByAnyone()) {
			for(final GroupEntity groupViewableByAnyone : groupRepository.listViewableByAnyone()) {
				if(groupViewableByAnyone.getName().equals(form.getName())
						&& ! groupViewableByAnyone.hasId(form.getId())) {
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
		return groupRepository.load(form.getId());
	}

	@Override
	protected void updateValues(final GroupEditInput form, final GroupEntity entity) {
		entity.setName(form.getName());
		entity.setViewableByAnyone(form.isViewableByAnyone());
	}
}

package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gagror.data.AbstractIdentifiableNamedInput;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelInput;
import com.gagror.data.wh40kskirmish.rules.experience.Wh40kSkirmishExperienceLevelOutput;

@NoArgsConstructor
public class Wh40kSkirmishGangTypeInput extends AbstractIdentifiableNamedInput<Long, Wh40kSkirmishGangTypeOutput> {

	@Getter
	@Setter
	private Long groupId;

	@Getter
	@Setter
	// TODO Annotate with @NotEmpty to ensure that at least one experience level exists
	private List<Wh40kSkirmishExperienceLevelInput> experienceLevels;
	// TODO Add experience level input lines dynamically on the edit page
	// http://outbottle.com/spring-3-mvc-adding-objects-to-a-list-element-on-the-fly-at-form-submit-generic-method/
	// http://stackoverflow.com/questions/171027/add-table-row-in-jquery
	// http://stackoverflow.com/questions/2160890/how-do-you-append-rows-to-a-table-using-jquery
	// http://mrbool.com/how-to-add-edit-and-delete-rows-of-a-html-table-with-jquery/26721

	public Wh40kSkirmishGangTypeInput(final Wh40kSkirmishGangTypeOutput currentState) {
		super(currentState);
		setGroupId(currentState.getGroup().getId());
		// Already existing experience levels
		setExperienceLevels(new ArrayList<Wh40kSkirmishExperienceLevelInput>());
		for(final Wh40kSkirmishExperienceLevelOutput experienceLevel : currentState.getExperienceLevels()) {
			getExperienceLevels().add(new Wh40kSkirmishExperienceLevelInput(experienceLevel));
		}
	}

	public Wh40kSkirmishGangTypeInput(final Long groupId) {
		setGroupId(groupId);
		// Don't provide any default experience levels
		setExperienceLevels(new ArrayList<Wh40kSkirmishExperienceLevelInput>());
	}
}

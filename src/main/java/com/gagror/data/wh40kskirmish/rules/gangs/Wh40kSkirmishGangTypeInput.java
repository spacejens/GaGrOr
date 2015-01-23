package com.gagror.data.wh40kskirmish.rules.gangs;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

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
	@NotEmpty
	private List<Wh40kSkirmishExperienceLevelInput> experienceLevels;
	// TODO When adding experience level on page, default experience points to one above the last line

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
		// TODO Add default to indicate that the table needs to have content, and change javascript to prevent removal
		setExperienceLevels(new ArrayList<Wh40kSkirmishExperienceLevelInput>());
	}
}

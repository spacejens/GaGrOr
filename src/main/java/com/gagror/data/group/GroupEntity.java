package com.gagror.data.group;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.CodingErrorException;
import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.wh40kskirmish.rules.Wh40kSkirmishRulesEntity;

@NoArgsConstructor
@ToString(of={}, callSuper=true)
@Entity
@Table(name="gaminggroup") // Table name doesn't match since 'group' is a reserved word
public class GroupEntity extends AbstractEditableNamedEntity {

	@OneToMany(mappedBy="group", fetch=FetchType.LAZY)
	@Getter
	private Set<GroupMemberEntity> groupMemberships;

	@Column(nullable = false)
	@Getter
	@Setter
	private boolean viewableByAnyone;

	@Column(nullable = false, updatable = false)
	@Getter
	private GroupType groupType;

	@OneToOne(mappedBy="group", cascade=CascadeType.PERSIST, optional=true)
	@Getter
	private Wh40kSkirmishRulesEntity wh40kSkirmishRules;

	public GroupEntity(final String name, final GroupType groupType) {
		super(name);
		this.groupType = groupType;
		switch(groupType) {
		case SOCIAL:
			// Nothing to do, no specific rules to create
			break;
		case WH40K_SKIRMISH:
			wh40kSkirmishRules = new Wh40kSkirmishRulesEntity(this);
			break;
		default:
			throw new CodingErrorException(String.format("Unsupported group type %s for group creation", groupType));
		}
		groupMemberships = new HashSet<>();
	}
}

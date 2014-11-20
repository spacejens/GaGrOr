package com.gagror.data.group;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;

@NoArgsConstructor
@ToString(of={"name"}, callSuper=true)
@Entity
@Table(name="gaminggroup") // Table name doesn't match since 'group' is a reserved word
public class GroupEntity extends AbstractEditableEntity {

	@Column(nullable = false)
	@Getter
	@Setter
	private String name;

	@OneToMany(mappedBy="group", fetch=FetchType.LAZY)
	@Getter
	private Set<GroupMemberEntity> groupMemberships;

	@Column(nullable = false)
	@Getter
	@Setter
	private boolean viewableByAnyone;

	@Column(nullable = false, updatable = false)
	@Getter
	@Setter
	private GroupType groupType;

	public GroupEntity(final String name, final GroupType groupType) {
		setName(name);
		setGroupType(groupType);
		groupMemberships = new HashSet<>();
	}
}

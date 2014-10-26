package com.gagror.data.group;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.gagror.data.AbstractEditableEntity;

@Data
@NoArgsConstructor
@Entity
@Table(name="gaminggroup") // Table name doesn't match since 'group' is a reserved word
@EqualsAndHashCode(of={}, callSuper=true)
public class GroupEntity extends AbstractEditableEntity {

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy="group", fetch=FetchType.LAZY)
	private Set<GroupMemberEntity> groupMemberships;
}

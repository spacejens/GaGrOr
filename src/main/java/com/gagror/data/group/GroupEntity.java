package com.gagror.data.group;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.gagror.data.Identifiable;

@Data
@NoArgsConstructor
@Entity
@Table(name="gaminggroup") // Table name doesn't match since 'group' is a reserved word
@EqualsAndHashCode(of="id")
public class GroupEntity implements Identifiable<Long> {

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private Long version;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, insertable = true, updatable = false)
	private Date created;

	@OneToMany(mappedBy="group", fetch=FetchType.LAZY)
	private Set<GroupMemberEntity> groupMemberships;
}

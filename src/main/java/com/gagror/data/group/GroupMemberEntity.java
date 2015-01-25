package com.gagror.data.group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.account.AccountEntity;

@NoArgsConstructor
@ToString(of={"group", "account", "memberType"}, callSuper=true)
@Entity
@Table(name="gagror_groupmember")
public class GroupMemberEntity extends AbstractEditableEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private GroupEntity group;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private AccountEntity account;

	@Column(nullable = false)
	@Getter
	@Setter
	private MemberType memberType;

	public GroupMemberEntity(
			final GroupEntity group,
			final AccountEntity account,
			final MemberType memberType) {
		this.group = group;
		this.account = account;
		setMemberType(memberType);
		// Add the new entity to the referencing collections
		group.getGroupMemberships().add(this);
		account.getGroupMemberships().add(this);
	}
}

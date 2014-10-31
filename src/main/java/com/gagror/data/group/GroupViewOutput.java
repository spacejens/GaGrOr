package com.gagror.data.group;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GroupViewOutput extends GroupReferenceOutput {

	private final Date created;

	public GroupViewOutput(final GroupEntity entity) {
		super(entity);
		created = entity.getCreated();
	}
}

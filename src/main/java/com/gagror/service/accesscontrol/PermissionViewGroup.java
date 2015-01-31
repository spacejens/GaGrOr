package com.gagror.service.accesscontrol;

import org.springframework.stereotype.Component;

@Component
public class PermissionViewGroup extends AbstractPermissionViewGroup {

	public PermissionViewGroup() {
		super("viewGroup", true);
	}
}

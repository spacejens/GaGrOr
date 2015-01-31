package com.gagror.service.accesscontrol;

import org.springframework.stereotype.Component;

@Component
public class PermissionViewGroupRules extends AbstractPermissionViewGroup {

	public PermissionViewGroupRules() {
		super("viewGroupRules", false);
	}
}

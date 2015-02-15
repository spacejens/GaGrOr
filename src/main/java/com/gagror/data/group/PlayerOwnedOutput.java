package com.gagror.data.group;

import com.gagror.data.account.AccountReferenceOutput;

public interface PlayerOwnedOutput extends GroupOwnedOutput {

	AccountReferenceOutput getPlayer();
}

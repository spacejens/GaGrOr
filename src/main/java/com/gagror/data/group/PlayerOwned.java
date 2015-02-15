package com.gagror.data.group;

import com.gagror.data.account.AccountEntity;

public interface PlayerOwned extends GroupOwned {

	AccountEntity getPlayer();
}

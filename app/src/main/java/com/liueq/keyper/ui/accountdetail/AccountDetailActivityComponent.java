package com.liueq.keyper.ui.accountdetail;

import com.liueq.keyper.base.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by liueq on 13/7/15.
 */
@ActivityScope
@Subcomponent(
    modules = {AccountDetailActivityModule.class
    }
)
public interface AccountDetailActivityComponent {
    AccountDetailActivity inject(AccountDetailActivity accountDetailActivity);

    AccountDetailActivityPresenter presenter();
}


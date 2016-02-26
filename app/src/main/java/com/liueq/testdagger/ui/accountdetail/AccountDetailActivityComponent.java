package com.liueq.testdagger.ui.accountdetail;

import com.liueq.testdagger.base.ActivityScope;

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


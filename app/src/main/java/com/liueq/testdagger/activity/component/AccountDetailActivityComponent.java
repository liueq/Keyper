package com.liueq.testdagger.activity.component;

import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.module.AccountDetailActivityModule;
import com.liueq.testdagger.ui.activity.presenter.AccountDetailActivityPresenter;

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


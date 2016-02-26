package com.liueq.testdagger.ui.main;

import com.liueq.testdagger.base.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by liueq on 13/7/15.
 */
@ActivityScope
@Subcomponent(
    modules = {
        MainActivityModule.class
    }
)
public interface MainActivityComponent {

    MainActivity inject(MainActivity mainActivity);

    MainActivityPresenter presenter();
}

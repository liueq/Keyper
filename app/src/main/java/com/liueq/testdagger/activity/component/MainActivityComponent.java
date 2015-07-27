package com.liueq.testdagger.activity.component;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.activity.module.MainActivityModule;
import com.liueq.testdagger.ui.activity.presenter.MainActivityPresenter;

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

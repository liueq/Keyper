package com.liueq.testdagger.activity.component;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.SplashActivity;
import com.liueq.testdagger.activity.module.SplashActivityModule;
import com.liueq.testdagger.ui.activity.presenter.SplashActivityPresenter;

import dagger.Subcomponent;

/**
 * Created by liueq on 16/7/15.
 */
@ActivityScope
@Subcomponent(
    modules = {
        SplashActivityModule.class
    }
)
public interface SplashActivityComponent {
    SplashActivity inject(SplashActivity splashActivity);

    SplashActivityPresenter presenter();
}

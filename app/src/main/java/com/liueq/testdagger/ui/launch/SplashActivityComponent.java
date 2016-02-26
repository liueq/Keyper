package com.liueq.testdagger.ui.launch;

import com.liueq.testdagger.base.ActivityScope;

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

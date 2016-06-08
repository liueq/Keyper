package com.liueq.keyper.ui.launch;

import com.liueq.keyper.base.ActivityScope;

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

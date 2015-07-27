package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.SplashActivity;
import com.liueq.testdagger.ui.activity.presenter.SplashActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 16/7/15.
 */
@Module
public class SplashActivityModule {

    private SplashActivity activity;

    public SplashActivityModule(SplashActivity splashActivity){
        activity = splashActivity;
    }

    @Provides
    @ActivityScope
    SplashActivity provideSplashActivity(){
        return activity;
    }

    @Provides
    @ActivityScope
    SplashActivityPresenter provideSplashActivityPresenter(){
        return new SplashActivityPresenter(activity);
    }
}

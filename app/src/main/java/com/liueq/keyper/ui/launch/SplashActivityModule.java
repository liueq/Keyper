package com.liueq.keyper.ui.launch;

import com.liueq.keyper.base.ActivityScope;

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

package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.SettingsActivity;
import com.liueq.testdagger.ui.activity.presenter.SettingsActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 29/7/15.
 */
@Module
public class SettingsActivityModule {

    private SettingsActivity settingsActivity;

    public SettingsActivityModule(SettingsActivity settingsActivity){
        this.settingsActivity = settingsActivity;
    }

    @Provides
    @ActivityScope
    SettingsActivity provideSettingsActivity(){
        return this.settingsActivity;
    }

    @Provides
    @ActivityScope
    SettingsActivityPresenter provideSettingsActivityPresenter(){
        return new SettingsActivityPresenter(settingsActivity);
    }
}

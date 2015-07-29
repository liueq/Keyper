package com.liueq.testdagger.activity.component;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.SettingsActivity;
import com.liueq.testdagger.activity.module.SettingsActivityModule;
import com.liueq.testdagger.ui.activity.presenter.SettingsActivityPresenter;

import dagger.Subcomponent;

/**
 * Created by liueq on 29/7/15.
 */
@ActivityScope
@Subcomponent(
        modules = {
                SettingsActivityModule.class
        }
)
public interface SettingsActivityComponent {

    SettingsActivity inject(SettingsActivity settingsActivity);

    SettingsActivityPresenter presenter();
}

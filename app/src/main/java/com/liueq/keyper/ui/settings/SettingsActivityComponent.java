package com.liueq.keyper.ui.settings;

import com.liueq.keyper.base.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by liueq on 29/7/15.
 * Settings Component
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

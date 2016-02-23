package com.liueq.testdagger;

import com.liueq.testdagger.activity.component.AccountDetailActivityComponent;
import com.liueq.testdagger.activity.component.MainActivityComponent;
import com.liueq.testdagger.activity.component.SettingsActivityComponent;
import com.liueq.testdagger.activity.component.SplashActivityComponent;
import com.liueq.testdagger.activity.module.AccountDetailActivityModule;
import com.liueq.testdagger.activity.module.MainActivityModule;
import com.liueq.testdagger.activity.module.SettingsActivityModule;
import com.liueq.testdagger.activity.module.SplashActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by liueq on 13/7/15.
 * Application Component
 *
 * All public module provided.
 */

@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {

    MainActivityComponent plus(MainActivityModule mainActivityModule);

    AccountDetailActivityComponent plus(AccountDetailActivityModule accountDetailActivityModule);

    SplashActivityComponent plus(SplashActivityModule splashActivityModule);

    SettingsActivityComponent plus(SettingsActivityModule settingsActivityModule);
}

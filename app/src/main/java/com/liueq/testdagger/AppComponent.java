package com.liueq.testdagger;

import com.liueq.testdagger.ui.accountdetail.AccountDetailActivityComponent;
import com.liueq.testdagger.ui.main.MainActivityComponent;
import com.liueq.testdagger.ui.settings.SettingsActivityComponent;
import com.liueq.testdagger.ui.launch.SplashActivityComponent;
import com.liueq.testdagger.ui.accountdetail.AccountDetailActivityModule;
import com.liueq.testdagger.ui.main.MainActivityModule;
import com.liueq.testdagger.ui.settings.SettingsActivityModule;
import com.liueq.testdagger.ui.launch.SplashActivityModule;
import com.liueq.testdagger.ui.tagdetail.TagDetailComponent;
import com.liueq.testdagger.ui.tagdetail.TagDetailModule;

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

    MainActivityComponent plus(MainActivityModule module);

    AccountDetailActivityComponent plus(AccountDetailActivityModule module);

    SplashActivityComponent plus(SplashActivityModule module);

    SettingsActivityComponent plus(SettingsActivityModule module);

    TagDetailComponent plus(TagDetailModule module);
}

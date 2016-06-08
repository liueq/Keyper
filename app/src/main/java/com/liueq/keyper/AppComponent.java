package com.liueq.keyper;

import com.liueq.keyper.ui.accountdetail.AccountDetailActivityComponent;
import com.liueq.keyper.ui.advancesearch.AdvanceSearchComponent;
import com.liueq.keyper.ui.advancesearch.AdvanceSearchModule;
import com.liueq.keyper.ui.main.MainActivityComponent;
import com.liueq.keyper.ui.settings.SettingsActivityComponent;
import com.liueq.keyper.ui.launch.SplashActivityComponent;
import com.liueq.keyper.ui.accountdetail.AccountDetailActivityModule;
import com.liueq.keyper.ui.main.MainActivityModule;
import com.liueq.keyper.ui.settings.SettingsActivityModule;
import com.liueq.keyper.ui.launch.SplashActivityModule;
import com.liueq.keyper.ui.tagdetail.TagDetailComponent;
import com.liueq.keyper.ui.tagdetail.TagDetailModule;

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

    AdvanceSearchComponent plus(AdvanceSearchModule module);
}

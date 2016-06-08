package com.liueq.keyper.ui.settings;

import com.liueq.keyper.base.ActivityScope;
import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;
import com.liueq.keyper.data.repository.StarRepo;
import com.liueq.keyper.data.repository.StarRepoDBImpl;
import com.liueq.keyper.data.repository.TagRepo;
import com.liueq.keyper.data.repository.TagRepoDBImpl;
import com.liueq.keyper.domain.interactor.CheckPasswordUC;
import com.liueq.keyper.domain.interactor.SharedPUC;
import com.liueq.keyper.domain.interactor.SetSpUC;
import com.liueq.keyper.utils.FileReader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 29/7/15.
 * Settings Module
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
    SettingsActivityPresenter provideSettingsActivityPresenter(FileReader fileReader){

        SharedPreferenceRepoImpl impl = new SharedPreferenceRepoImpl(settingsActivity);

        //UseCase
        SharedPUC sharedPUC = new SharedPUC(impl);
        SetSpUC setSpUC = new SetSpUC(impl);
        CheckPasswordUC checkPasswordUC = new CheckPasswordUC(impl);

        //Repository
        StarRepo sr = new StarRepoDBImpl(settingsActivity);
        TagRepo tr = new TagRepoDBImpl(settingsActivity);

        return new SettingsActivityPresenter(settingsActivity,
                sharedPUC
        );
    }
}

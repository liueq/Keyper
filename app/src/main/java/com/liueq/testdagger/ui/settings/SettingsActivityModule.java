package com.liueq.testdagger.ui.settings;

import com.liueq.testdagger.base.ActivityScope;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.AccountRepoImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.data.repository.StarRepo;
import com.liueq.testdagger.data.repository.StarRepoDBImpl;
import com.liueq.testdagger.data.repository.TagRepo;
import com.liueq.testdagger.data.repository.TagRepoDBImpl;
import com.liueq.testdagger.domain.interactor.CheckPasswordUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.GetSpUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;
import com.liueq.testdagger.domain.interactor.SetSpUC;
import com.liueq.testdagger.utils.FileReader;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 29/7/15.
 * 设置
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
    SettingsActivityPresenter provideSettingsActivityPresenter(List<Account> accountList, FileReader fileReader){


        SharedPreferenceRepoImpl impl = new SharedPreferenceRepoImpl(settingsActivity);

        GetSpUC getSpUC = new GetSpUC(impl);
        SetSpUC setSpUC = new SetSpUC(impl);

        AccountRepo ar = new AccountRepoImpl(fileReader, getSpUC);
        StarRepo sr = new StarRepoDBImpl(settingsActivity);
        TagRepo tr = new TagRepoDBImpl(settingsActivity);

        SaveAccountListUC saveAccountListUC = new SaveAccountListUC(ar, getSpUC, sr, tr);
        GetAccountListUC getAccountListUC = new GetAccountListUC(ar, sr, getSpUC);
        CheckPasswordUC checkPasswordUC = new CheckPasswordUC(impl);

        return new SettingsActivityPresenter(settingsActivity,
                accountList,
                setSpUC,
                getSpUC,
                checkPasswordUC,
                saveAccountListUC,
                getAccountListUC
        );
    }
}

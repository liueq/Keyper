package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.SettingsActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;
import com.liueq.testdagger.domain.interactor.CheckPasswordUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.GetSpUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;
import com.liueq.testdagger.domain.interactor.SetSpUC;
import com.liueq.testdagger.ui.activity.presenter.SettingsActivityPresenter;
import com.liueq.testdagger.utils.FileReader;

import java.util.List;

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
    SettingsActivityPresenter provideSettingsActivityPresenter(List<Account> accountList, FileReader fileReader){


        SharedPreferenceRepositoryImpl impl = new SharedPreferenceRepositoryImpl(settingsActivity);

        GetSpUC getSpUC = new GetSpUC(impl);
        SetSpUC setSpUC = new SetSpUC(impl);

        AccountRepository ar = new AccountRepositoryImpl(fileReader, getSpUC);

        SaveAccountListUC saveAccountListUC = new SaveAccountListUC(ar, getSpUC);
        GetAccountListUC getAccountListUC = new GetAccountListUC(ar, getSpUC);
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

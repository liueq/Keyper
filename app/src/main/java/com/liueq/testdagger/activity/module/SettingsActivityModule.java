package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.SettingsActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepository;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;
import com.liueq.testdagger.domain.interactor.CheckPasswordUseCase;
import com.liueq.testdagger.domain.interactor.GetAccountListUseCase;
import com.liueq.testdagger.domain.interactor.GetSpUseCase;
import com.liueq.testdagger.domain.interactor.SaveAccountListUseCase;
import com.liueq.testdagger.domain.interactor.SetSpUseCase;
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

        GetSpUseCase getSpUseCase = new GetSpUseCase(impl);
        SetSpUseCase setSpUseCase = new SetSpUseCase(impl);

        AccountRepositoryImpl ari = new AccountRepositoryImpl(fileReader, getSpUseCase);

        SaveAccountListUseCase saveAccountListUseCase = new SaveAccountListUseCase(ari, getSpUseCase);
        GetAccountListUseCase getAccountListUseCase = new GetAccountListUseCase(ari, getSpUseCase);
        CheckPasswordUseCase checkPasswordUseCase = new CheckPasswordUseCase(impl);

        return new SettingsActivityPresenter(settingsActivity,
                accountList,
                setSpUseCase,
                getSpUseCase,
                checkPasswordUseCase,
                saveAccountListUseCase,
                getAccountListUseCase
        );
    }
}

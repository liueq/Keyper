package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepository;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;
import com.liueq.testdagger.domain.interactor.DeleteAccountUseCase;
import com.liueq.testdagger.domain.interactor.GetAccountListUseCase;
import com.liueq.testdagger.domain.interactor.GetSpUseCase;
import com.liueq.testdagger.domain.interactor.SaveAccountListUseCase;
import com.liueq.testdagger.domain.interactor.SetSpUseCase;
import com.liueq.testdagger.ui.activity.presenter.AccountDetailActivityPresenter;
import com.liueq.testdagger.utils.FileReader;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 13/7/15.
 */
@Module
public class AccountDetailActivityModule {

    private AccountDetailActivity accountDetailActivity;

    public AccountDetailActivityModule(AccountDetailActivity activity){
        this.accountDetailActivity = activity;
    }

    @Provides
    @ActivityScope
    AccountDetailActivity provideAccountDetailActivity(){
        return accountDetailActivity;
    }

    @Provides
    @ActivityScope
    AccountDetailActivityPresenter provideAccountDetailActivityPresenter(FileReader fileReader, List<Account> accountList){
        AccountRepository ar = new AccountRepositoryImpl(fileReader);
        SharedPreferenceRepository spr = new SharedPreferenceRepositoryImpl(accountDetailActivity);

        GetSpUseCase getSpUseCase = new GetSpUseCase((SharedPreferenceRepositoryImpl) spr);

        SaveAccountListUseCase saveAccountListUseCase = new SaveAccountListUseCase((AccountRepositoryImpl) ar, getSpUseCase);
        GetAccountListUseCase getAccountListUseCase = new GetAccountListUseCase((AccountRepositoryImpl) ar, getSpUseCase);
        DeleteAccountUseCase deleteAccountUseCase = new DeleteAccountUseCase();

        return new AccountDetailActivityPresenter(accountDetailActivity, accountList, saveAccountListUseCase, getAccountListUseCase, deleteAccountUseCase);
    }
}

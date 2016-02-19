package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.data.repository.AccountRepositoryDBImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepository;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;
import com.liueq.testdagger.domain.interactor.DeleteAccountUC;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.GetSpUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;
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

        SharedPreferenceRepository spr = new SharedPreferenceRepositoryImpl(accountDetailActivity);
        GetSpUC getSpUC = new GetSpUC((SharedPreferenceRepositoryImpl) spr);

        AccountRepository ar = new AccountRepositoryDBImpl(accountDetailActivity, getSpUC);

        SaveAccountListUC saveAccountListUC = new SaveAccountListUC(ar, getSpUC);
        GetAccountListUC getAccountListUC = new GetAccountListUC(ar, getSpUC);
        DeleteAccountUC deleteAccountUC = new DeleteAccountUC(ar);
        GetAccountDetailUC getAccountDetailUC = new GetAccountDetailUC(ar);

        return new AccountDetailActivityPresenter(accountDetailActivity, accountList, saveAccountListUC, getAccountListUC, deleteAccountUC, getAccountDetailUC);
    }
}

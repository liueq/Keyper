package com.liueq.testdagger.ui.accountdetail;

import com.liueq.testdagger.base.ActivityScope;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.AccountRepoDBImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepo;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.data.repository.StarRepo;
import com.liueq.testdagger.data.repository.StarRepoDBImpl;
import com.liueq.testdagger.data.repository.TagRepo;
import com.liueq.testdagger.data.repository.TagRepoDBImpl;
import com.liueq.testdagger.domain.interactor.AddTagUC;
import com.liueq.testdagger.domain.interactor.DeleteAccountUC;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.GetSpUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;
import com.liueq.testdagger.utils.FileReader;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 13/7/15.
 * Account Detail 的Module
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

        SharedPreferenceRepo spr = new SharedPreferenceRepoImpl(accountDetailActivity);
        GetSpUC getSpUC = new GetSpUC((SharedPreferenceRepoImpl) spr);

        AccountRepo ar = new AccountRepoDBImpl(accountDetailActivity, getSpUC);
        StarRepo sr = new StarRepoDBImpl(accountDetailActivity);
        TagRepo tr = new TagRepoDBImpl(accountDetailActivity);

        SaveAccountListUC saveAccountListUC = new SaveAccountListUC(ar, getSpUC, sr);
        GetAccountListUC getAccountListUC = new GetAccountListUC(ar, sr,  getSpUC);
        DeleteAccountUC deleteAccountUC = new DeleteAccountUC(ar);
        GetAccountDetailUC getAccountDetailUC = new GetAccountDetailUC(ar, sr, tr);
        AddTagUC addTagUC = new AddTagUC(tr);

        return new AccountDetailActivityPresenter(accountDetailActivity, accountList,
                saveAccountListUC,
                getAccountListUC,
                deleteAccountUC,
                getAccountDetailUC,
                addTagUC);
    }
}

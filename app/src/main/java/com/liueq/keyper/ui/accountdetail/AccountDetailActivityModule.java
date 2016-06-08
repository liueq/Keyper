package com.liueq.keyper.ui.accountdetail;

import com.liueq.keyper.base.ActivityScope;
import com.liueq.keyper.data.repository.AccountRepo;
import com.liueq.keyper.data.repository.AccountRepoDBImpl;
import com.liueq.keyper.data.repository.SharedPreferenceRepo;
import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;
import com.liueq.keyper.data.repository.StarRepo;
import com.liueq.keyper.data.repository.StarRepoDBImpl;
import com.liueq.keyper.data.repository.TagRepo;
import com.liueq.keyper.data.repository.TagRepoDBImpl;
import com.liueq.keyper.domain.interactor.AddTagUC;
import com.liueq.keyper.domain.interactor.DeleteAccountUC;
import com.liueq.keyper.domain.interactor.GetAccountDetailUC;
import com.liueq.keyper.domain.interactor.GetAccountListUC;
import com.liueq.keyper.domain.interactor.SharedPUC;
import com.liueq.keyper.domain.interactor.SaveAccountListUC;
import com.liueq.keyper.utils.FileReader;

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
    AccountDetailActivityPresenter provideAccountDetailActivityPresenter(FileReader fileReader){

        SharedPreferenceRepo spr = new SharedPreferenceRepoImpl(accountDetailActivity);
        SharedPUC sharedPUC = new SharedPUC((SharedPreferenceRepoImpl) spr);

        AccountRepo ar = new AccountRepoDBImpl(accountDetailActivity, sharedPUC);
        StarRepo sr = new StarRepoDBImpl(accountDetailActivity);
        TagRepo tr = new TagRepoDBImpl(accountDetailActivity);

        SaveAccountListUC saveAccountListUC = new SaveAccountListUC(ar, sharedPUC, sr, tr);
        GetAccountListUC getAccountListUC = new GetAccountListUC(ar, sr, sharedPUC);
        DeleteAccountUC deleteAccountUC = new DeleteAccountUC(ar);
        GetAccountDetailUC getAccountDetailUC = new GetAccountDetailUC(ar, sr, tr);
        AddTagUC addTagUC = new AddTagUC(tr, sr);

        return new AccountDetailActivityPresenter(accountDetailActivity,
                saveAccountListUC,
                getAccountListUC,
                deleteAccountUC,
                getAccountDetailUC,
                addTagUC);
    }
}

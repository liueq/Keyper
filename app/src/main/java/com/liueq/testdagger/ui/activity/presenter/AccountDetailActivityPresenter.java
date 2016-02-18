package com.liueq.testdagger.ui.activity.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.DeleteAccountUseCase;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUseCase;
import com.liueq.testdagger.domain.interactor.GetAccountListUseCase;
import com.liueq.testdagger.domain.interactor.SaveAccountListUseCase;
import com.liueq.testdagger.utils.Encrypter;
import com.liueq.testdagger.utils.FileReader;
import com.liueq.testdagger.utils.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 13/7/15.
 */
public class AccountDetailActivityPresenter extends Presenter{

    public final static String TAG = "AccountDAPresenter";

    private AccountDetailActivity activity;
    List<Account> mAccountList;
    public String mId;
    private Account mCurrentAccount;

    SaveAccountListUseCase saveAccountListUseCase;
    GetAccountListUseCase getAccountListUseCase;
    DeleteAccountUseCase deleteAccountUseCase;
    GetAccountDetailUseCase getAccountDetailUseCase;

    public AccountDetailActivityPresenter(AccountDetailActivity activity,
                                          List<Account> accountList,
                                          SaveAccountListUseCase saveAccountListUseCase,
                                          GetAccountListUseCase getAccountListUseCase,
                                          DeleteAccountUseCase deleteAccountUseCase,
                                          GetAccountDetailUseCase getAccountDetailUseCase){
        this.activity = activity;
        this.mAccountList = accountList;

        this.saveAccountListUseCase = saveAccountListUseCase;
        this.getAccountListUseCase = getAccountListUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.getAccountDetailUseCase = getAccountDetailUseCase;
    }

    public void init(Bundle bundle){
        Account account = (Account) bundle.getSerializable("account");
        mId = account.id;
    }

    public Account loadData(String id){
        mCurrentAccount = getAccountDetailUseCase.execute(id);
        return mCurrentAccount;
    }

    /**
     * 包括更新和插入
     * @param account
     * @return
     */
    public boolean saveData(Account account){
        if(!TextUtils.isEmpty(account.site)){
            return saveAccountListUseCase.executeDB(account);
        }else{
            return false;
        }
    }

    public Account getCurrentAccount(){
        return mCurrentAccount;
    }

    public boolean deleteAccount(){
        return deleteAccountUseCase.executeDB(mCurrentAccount);
    }

}

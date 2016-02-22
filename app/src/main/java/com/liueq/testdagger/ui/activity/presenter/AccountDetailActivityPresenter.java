package com.liueq.testdagger.ui.activity.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.DeleteAccountUC;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;

import java.util.List;

/**
 * Created by liueq on 13/7/15.
 * Account Detail Presenter
 */
public class AccountDetailActivityPresenter extends Presenter{

    public final static String TAG = "AccountDAPresenter";

    private AccountDetailActivity activity;
    List<Account> mAccountList;
    public String mId;
    private Account mCurrentAccount;

    SaveAccountListUC saveAccountListUC;
    GetAccountListUC getAccountListUC;
    DeleteAccountUC deleteAccountUC;
    GetAccountDetailUC getAccountDetailUC;

    public AccountDetailActivityPresenter(AccountDetailActivity activity,
                                          List<Account> accountList,
                                          SaveAccountListUC saveAccountListUC,
                                          GetAccountListUC getAccountListUC,
                                          DeleteAccountUC deleteAccountUC,
                                          GetAccountDetailUC getAccountDetailUC){
        this.activity = activity;
        this.mAccountList = accountList;

        this.saveAccountListUC = saveAccountListUC;
        this.getAccountListUC = getAccountListUC;
        this.deleteAccountUC = deleteAccountUC;
        this.getAccountDetailUC = getAccountDetailUC;
    }

	/**
     * 仅仅是获取mId
     * @param bundle
     */
    public void init(Bundle bundle){
        Account account = (Account) bundle.getSerializable("account");
        mId = account.id;
    }

    public Account loadData(String id){
        mCurrentAccount = getAccountDetailUC.execute(id);
        if(mCurrentAccount == null){
            mCurrentAccount = new Account();
        }
        return mCurrentAccount;
    }

    /**
     * 包括更新和插入
     * @param account
     * @return
     */
    public boolean saveData(Account account){
        if(!TextUtils.isEmpty(account.site)){
            String result_id = saveAccountListUC.executeDB(account);
            if(TextUtils.isEmpty(result_id)){
                return false;
            }else{
                mId = result_id;
                loadData(result_id);
                return true;
            }
        }else{
            return false;
        }
    }

    public Account getCurrentAccount(){
        return mCurrentAccount;
    }

    public boolean deleteAccount(){
        return deleteAccountUC.executeDB(mCurrentAccount);
    }

}

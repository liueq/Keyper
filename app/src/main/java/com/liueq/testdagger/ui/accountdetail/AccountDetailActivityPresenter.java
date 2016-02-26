package com.liueq.testdagger.ui.accountdetail;

import android.os.Bundle;
import android.text.TextUtils;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.domain.interactor.AddTagUC;
import com.liueq.testdagger.domain.interactor.DeleteAccountUC;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;
import com.liueq.testdagger.base.Presenter;

import java.util.List;

/**
 * Created by liueq on 13/7/15.
 * Account Detail Presenter
 */
public class AccountDetailActivityPresenter extends Presenter {

    public final static String TAG = "AccountDAPresenter";

    private AccountDetailActivity activity;
    List<Account> mAccountList;
    public String mId;
    private Account mCurrentAccount;

    SaveAccountListUC saveAccountListUC;
    GetAccountListUC getAccountListUC;
    DeleteAccountUC deleteAccountUC;
    GetAccountDetailUC getAccountDetailUC;
    AddTagUC addTagUC;

    public AccountDetailActivityPresenter(AccountDetailActivity activity,
                                          List<Account> accountList,
                                          SaveAccountListUC saveAccountListUC,
                                          GetAccountListUC getAccountListUC,
                                          DeleteAccountUC deleteAccountUC,
                                          GetAccountDetailUC getAccountDetailUC,
                                          AddTagUC addTagUC){
        this.activity = activity;
        this.mAccountList = accountList;

        this.saveAccountListUC = saveAccountListUC;
        this.getAccountListUC = getAccountListUC;
        this.deleteAccountUC = deleteAccountUC;
        this.getAccountDetailUC = getAccountDetailUC;
        this.addTagUC = addTagUC;
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
     * Get tag list from account
     * @param account
     * @return
     */
    public List<Tag> getTagList(Account account){
        return getAccountDetailUC.getTagList(account);
    }

	/**
     * Get available tags
     * @return
     */
    public List<Tag> getAvailableTags(){
        return addTagUC.getAvailableTags(mCurrentAccount);
    }

	/**
     * Add tag to account, tag could be a new tag
     * @param tag
     * @return
     */
    public boolean addTag(Tag tag){
       return addTagUC.addTag(mCurrentAccount, tag);
    }

	/**
	 * Search available tags
     * @param str
     * @return
     */
    public List<Tag> searchAvailableTag(String str){
        return addTagUC.searchTags(mCurrentAccount, str);
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

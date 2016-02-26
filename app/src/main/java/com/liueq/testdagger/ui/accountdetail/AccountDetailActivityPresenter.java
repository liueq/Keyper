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

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    /******************** AccountDetailFragment Operation ********************/

    public void loadDataFromDB(AccountDetailFragment accountDetailFragment){
        getDetailOb(mId)
                .map(addTagOb())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getDetailSub());
    }

    public void saveDataToDB(Account account){
        saveDetailOb(account)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(saveDetailSub());
    }

    private Observable<Account> getDetailOb(final String id){
        return Observable.create(new Observable.OnSubscribe<Account>() {
            @Override
            public void call(Subscriber<? super Account> subscriber) {
                Account account = loadDataFromDB(id);
                subscriber.onNext(account);
            }
        });
    }

    private Func1<Account, Account> addTagOb(){
        return new Func1<Account, Account>() {
            @Override
            public Account call(Account account) {
                // Get Account Tag
                List<Tag> list = getTagList(account);
                list.add(0, new Tag("-1"));
                account.tag_list.addAll(list);
                return account;
            }
        };
    }

    private Subscriber<Account> getDetailSub(){
        return new Subscriber<Account>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Account account) {
                ((AccountDetailFragment) getFragment(AccountDetailFragment.class)).updateUI(account);
            }
        };
    }

    private Observable<Boolean> saveDetailOb(final Account account){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean result = false;
                if(!TextUtils.isEmpty(account.site)){
                    String result_id = saveAccountListUC.executeDB(account);
                    if(TextUtils.isEmpty(result_id)){
                        result = false;
                    }else{
                        mId = result_id;
                        loadDataFromDB(result_id);
                        result = true;
                    }
                }else{
                    result = false;
                }
                subscriber.onNext(result);
            }
        });
    }

    private Subscriber<Boolean> saveDetailSub(){
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                ((AccountDetailFragment) getFragment(AccountDetailFragment.class)).showResult(aBoolean);
            }
        };
    }

    /******************** ChooseTagDialog Operation ********************/

    public void searchAvailableTag(String search){
        searchAvailableTagOb(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchAvailableTagSub());
    }

    public void createNewTag(Tag tag){
        createNewTagOb(tag).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createNewTagSub());
    }

    private Observable<List<Tag>> searchAvailableTagOb(final String search){
        return Observable.create(new Observable.OnSubscribe<List<Tag>>() {
            @Override
            public void call(Subscriber<? super List<Tag>> subscriber) {
                subscriber.onNext(searchAvailableTagFromDB(search));
            }
        });
    }

    private Subscriber<List<Tag>> searchAvailableTagSub(){
        return new Subscriber<List<Tag>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Tag> list) {
                ((ChooseTagDialog) getFragment(ChooseTagDialog.class)).updateUI(list);
            }
        };
    }

    private Observable<Tag> createNewTagOb(final Tag tag){
        return Observable.create(new Observable.OnSubscribe<Tag>() {
            @Override
            public void call(Subscriber<? super Tag> subscriber) {
                addTag(tag);
                subscriber.onNext(tag);
            }
        });
    }

    private Subscriber<Tag> createNewTagSub(){
        return new Subscriber<Tag>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Tag tag) {
                ((ChooseTagDialog) getFragment(ChooseTagDialog.class)).getDialog().dismiss();
                ((AccountDetailFragment) getFragment(AccountDetailFragment.class)).updateUI(mCurrentAccount);
            }
        };
    }



    public Account loadDataFromDB(String id){
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
    public List<Tag> searchAvailableTagFromDB(String str){
        return addTagUC.searchTags(mCurrentAccount, str);
    }


    public Account getCurrentAccount(){
        return mCurrentAccount;
    }

    public boolean deleteAccount(){
        return deleteAccountUC.executeDB(mCurrentAccount);
    }

}

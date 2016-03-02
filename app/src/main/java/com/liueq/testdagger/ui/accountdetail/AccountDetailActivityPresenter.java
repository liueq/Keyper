package com.liueq.testdagger.ui.accountdetail;

import android.os.Bundle;
import android.text.TextUtils;

import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.domain.interactor.AddTagUC;
import com.liueq.testdagger.domain.interactor.DeleteAccountUC;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 13/7/15.
 * Account Detail Presenter
 */
public class AccountDetailActivityPresenter extends Presenter {

    public final static String TAG = "AccountDAPresenter";

    private AccountDetailActivity activity;
    public String mId;
    private Account mCurrentAccount;

    SaveAccountListUC saveAccountListUC;
    GetAccountListUC getAccountListUC;
    DeleteAccountUC deleteAccountUC;
    GetAccountDetailUC getAccountDetailUC;
    AddTagUC addTagUC;

    public AccountDetailActivityPresenter(AccountDetailActivity activity,
                                          SaveAccountListUC saveAccountListUC,
                                          GetAccountListUC getAccountListUC,
                                          DeleteAccountUC deleteAccountUC,
                                          GetAccountDetailUC getAccountDetailUC,
                                          AddTagUC addTagUC){
        this.activity = activity;
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

    /******************** AccountDetailActivity Action ********************/
    public void deleteAccountAction(){
        deleteOb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteSub());
    }

    /******************** AccountDetailActivity RxJava ********************/
    private Observable<Boolean> deleteOb(){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(deleteAccount());
            }
        });
    }

    private Subscriber<Boolean> deleteSub(){
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                activity.deleteToast(aBoolean);
            }
        };
    }

    /******************** AccountDetailFragment Action ********************/

    public void loadDataFromDB(AccountDetailFragment accountDetailFragment){
        getDetailOb(mId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getDetailSub());
    }

    public void saveDataToDB(Account account){
        ((AccountDetailFragment) getFragment(AccountDetailFragment.class)).syncData();

        saveDetailOb(account)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(saveDetailSub());
    }

    public void removeTagAction(Tag tag){
        removeTagOb(tag).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(removeTagSub());
    }

    /******************** AccountDetailFragment RxJava ********************/

    private Observable<Account> getDetailOb(final String id){
        return Observable.create(new Observable.OnSubscribe<Account>() {
            @Override
            public void call(Subscriber<? super Account> subscriber) {
                Account account = loadDataFromDB(id);
                subscriber.onNext(account);
            }
        });
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

    private Observable<Tag> removeTagOb(final Tag tag){
        return Observable.create(new Observable.OnSubscribe<Tag>() {
            @Override
            public void call(Subscriber<? super Tag> subscriber) {
                mCurrentAccount.tag_list.remove(tag);
                subscriber.onNext(null);
            }
        });
    }

    private Action1<Tag> removeTagSub(){
        return new Action1<Tag>() {
            @Override
            public void call(Tag tag) {
                ((AccountDetailFragment) getFragment(AccountDetailFragment.class)).updateUI(mCurrentAccount);
            }
        };
    }


    /******************** ChooseTagDialog Action ********************/

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

    public void hasTag(String tag_name){
        hasTagOb(tag_name).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hasTagSub());
    }

    public void addTagAction(Tag tag){
        addTagOb(tag).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addTagSub());
    }

	/**
	 * 当搜索输入为空的时候，默认获取所有的可选tag
     */
    public void getAvailableTagAction(){
        getAvailableTagOb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAvailableTagSub());
    }

    /******************** ChooseTagDialog RxJava ********************/

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
                ((ChooseTagDialog) getFragment(ChooseTagDialog.class)).updateList(list);
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

    private Observable<Boolean> hasTagOb(final String tag_name){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(addTagUC.hasTag(tag_name));
            }
        });
    }

    private Action1<Boolean> hasTagSub(){
        return new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                ((ChooseTagDialog) getFragment(ChooseTagDialog.class)).updateAddTag(aBoolean);
            }
        };
    }

    private Observable<Tag> addTagOb(final Tag tag){
        return Observable.create(new Observable.OnSubscribe<Tag>() {
            @Override
            public void call(Subscriber<? super Tag> subscriber) {
                addTag(tag);
                subscriber.onNext(null);
            }
        });
    }

    private Action1<Tag> addTagSub(){
        return new Action1<Tag>() {
            @Override
            public void call(Tag tag) {
                ((ChooseTagDialog) getFragment(ChooseTagDialog.class)).dismiss();
                ((AccountDetailFragment) getFragment(AccountDetailFragment.class)).updateUI(mCurrentAccount);
            }
        };
    }

    private Observable<List<Tag>> getAvailableTagOb(){
        return Observable.create(new Observable.OnSubscribe<List<Tag>>() {
            @Override
            public void call(Subscriber<? super List<Tag>> subscriber) {
                subscriber.onNext(getAvailableTags());
            }
        });
    }

    private Action1<List<Tag>> getAvailableTagSub(){
        return new Action1<List<Tag>>() {
            @Override
            public void call(List<Tag> tags) {
                ((ChooseTagDialog) getFragment(ChooseTagDialog.class)).updateList(tags);
            }
        };
    }

    /******************** UseCase Operation ********************/

    public Account loadDataFromDB(String id){
        mCurrentAccount = getAccountDetailUC.execute(id);
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

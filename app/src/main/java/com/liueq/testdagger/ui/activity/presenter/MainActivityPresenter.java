package com.liueq.testdagger.ui.activity.presenter;

import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.GetAccountListUseCase;
import com.liueq.testdagger.domain.interactor.SearchAccountUseCase;

import java.util.List;

import rx.Subscription;

/**
 * Created by liueq on 13/7/15.
 */
public class MainActivityPresenter extends Presenter{

    public final static String TAG = "MainActivityPresenter";

    private MainActivity mainActivity;
    List<Account> mAccountList;
    GetAccountListUseCase getAccountListUseCase;
    SearchAccountUseCase searchAccountUseCase;

    Subscription mSubscription;

    public MainActivityPresenter(MainActivity mainActivity, List<Account> list, GetAccountListUseCase getAccountListUseCase, SearchAccountUseCase searchAccountUseCase) {
        this.mainActivity = mainActivity;
        this.mAccountList = list;
        this.getAccountListUseCase = getAccountListUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
    }

    public List<Account> loadList(){
        return getAccountListUseCase.executeDB();
    }

    public void loadData(){
//        mAccountList.clear();
//        mAccountList.addAll((List<Account>) getAccountListUseCase.executeDB());
//        mainActivity.updateUI(mAccountList);
    }

    public void search(String searchKey){
//        mainActivity.updateUI(searchAccountUseCase.execute(searchKey) );
    }

}

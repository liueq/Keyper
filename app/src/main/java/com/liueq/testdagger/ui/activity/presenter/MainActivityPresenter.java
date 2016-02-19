package com.liueq.testdagger.ui.activity.presenter;

import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.SearchAccountUC;

import java.util.List;

/**
 * Created by liueq on 13/7/15.
 */
public class MainActivityPresenter extends Presenter{

    public final static String TAG = "MainActivityPresenter";

    private MainActivity mainActivity;
    List<Account> mAccountList;
    GetAccountListUC getAccountListUC;
    SearchAccountUC searchAccountUC;

    public MainActivityPresenter(MainActivity mainActivity, List<Account> list, GetAccountListUC getAccountListUC, SearchAccountUC searchAccountUC) {
        this.mainActivity = mainActivity;
        this.mAccountList = list;
        this.getAccountListUC = getAccountListUC;
        this.searchAccountUC = searchAccountUC;
    }

    public List<Account> loadList(){
        return getAccountListUC.executeDB();
    }

    public void loadData(){
//        mAccountList.clear();
//        mAccountList.addAll((List<Account>) getAccountListUseCase.executeDB());
//        mainActivity.updateUI(mAccountList);
    }

    public List<Account> search(String searchKey){
        return searchAccountUC.execute(searchKey);
    }

}

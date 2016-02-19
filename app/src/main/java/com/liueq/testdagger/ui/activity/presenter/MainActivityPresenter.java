package com.liueq.testdagger.ui.activity.presenter;

import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.SearchAccountUC;
import com.liueq.testdagger.domain.interactor.StarUC;

import java.util.List;

/**
 * Created by liueq on 13/7/15.
 * MainActivity Presenter
 *      include: ListFragment, SearchDialogFragment
 */
public class MainActivityPresenter extends Presenter{

    public final static String TAG = "MainActivityPresenter";

    private MainActivity mainActivity;
    List<Account> mAccountList;

    GetAccountListUC getAccountListUC;
    SearchAccountUC searchAccountUC;
    StarUC mStarUC;

    public MainActivityPresenter(MainActivity mainActivity, List<Account> list, GetAccountListUC getAccountListUC, SearchAccountUC searchAccountUC, StarUC starUC) {
        this.mainActivity = mainActivity;
        this.mAccountList = list;

        this.getAccountListUC = getAccountListUC;
        this.searchAccountUC = searchAccountUC;
        this.mStarUC = starUC;
    }

    public List<Account> loadList(){
        return getAccountListUC.executeDB();
    }

    public void loadData(){
    }

    public List<Account> search(String searchKey){
        return searchAccountUC.execute(searchKey);
    }

    public boolean starOrUnStar(Account account){
        boolean result = false;

        if(account.is_stared){
            result = mStarUC.unStarAccount(account);
        }else{
            result = mStarUC.starAccount(account);
        }

        return result;
    }

}

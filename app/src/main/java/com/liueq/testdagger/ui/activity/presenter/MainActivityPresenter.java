package com.liueq.testdagger.ui.activity.presenter;

import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.GetStarListUC;
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

    GetAccountListUC getAccountListUC;
    SearchAccountUC searchAccountUC;
    StarUC mStarUC;
    GetStarListUC getStarListUC;

    public MainActivityPresenter(MainActivity mainActivity, GetAccountListUC getAccountListUC, SearchAccountUC searchAccountUC, StarUC starUC, GetStarListUC starListUC) {
        this.mainActivity = mainActivity;

        this.getAccountListUC = getAccountListUC;
        this.searchAccountUC = searchAccountUC;
        this.mStarUC = starUC;
        this.getStarListUC = starListUC;
    }

    public List<Account> loadList(){
        return getAccountListUC.executeDB();
    }

    public List<Account> loadStarList(){
        return getStarListUC.executeDB();
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

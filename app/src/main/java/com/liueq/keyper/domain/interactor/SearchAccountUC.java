package com.liueq.keyper.domain.interactor;

import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.data.repository.AccountRepo;
import com.liueq.keyper.data.repository.StarRepo;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by liueq on 27/7/15.
 * 搜索 UseCase
 */
public class SearchAccountUC extends UseCase {

    AccountRepo mAR;
    StarRepo mSR;

    @Inject
    public SearchAccountUC(AccountRepo AR, StarRepo SR){
        this.mAR = AR;
        this.mSR = SR;
    }

    public List<Account> execute(String key){
        List<Account> list = mAR.searchAccount(key);
        list = mSR.getStarStatusList(list);
        return list;
    }

    public List<Account> searchByField(String key, String field){
        List<Account> list = mAR.searchAccountByField(key, field);
        list = mSR.getStarStatusList(list);
        return list;
    }
}

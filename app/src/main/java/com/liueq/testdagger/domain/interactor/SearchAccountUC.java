package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.StarRepo;

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
}

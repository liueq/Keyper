package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by liueq on 27/7/15.
 */
public class SearchAccountUC extends UseCase {

    AccountRepository mAR;

    @Inject
    public SearchAccountUC(AccountRepository AR){
        this.mAR = AR;
    }

    public List<Account> execute(String key){
        return mAR.searchAccount(key);
    }
}

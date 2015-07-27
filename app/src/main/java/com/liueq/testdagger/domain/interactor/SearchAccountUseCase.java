package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.repository.AccountRepositoryImpl;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by liueq on 27/7/15.
 */
public class SearchAccountUseCase extends UseCase {

    AccountRepositoryImpl mARI;

    @Inject
    public SearchAccountUseCase(AccountRepositoryImpl ARI){
        this.mARI = ARI;
    }

    public List execute(String key){
        return mARI.searchAccount(key);
    }
}

package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.repository.AccountRepositoryImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 */
public class GetAccountListUseCase extends UseCase {

    AccountRepositoryImpl mARI;

    @Inject
    public GetAccountListUseCase(AccountRepositoryImpl ARI){
        this.mARI = ARI;
    }

    public List execute() {
        return mARI.getAccountList();
    }

}

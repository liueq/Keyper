package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.repository.AccountRepositoryImpl;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 实际没有使用
 */
public class GetAccountDetailUseCase extends UseCase {

    AccountRepositoryImpl mARI;

    @Inject
    public GetAccountDetailUseCase(AccountRepositoryImpl ARI){
        mARI = ARI;
    }

    public Object execute(String userId) {
        return mARI.getAccountDetail(userId);
    }
}

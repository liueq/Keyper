package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepo;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 实际没有使用
 */
public class GetAccountDetailUC extends UseCase {

    AccountRepo mAR;

    @Inject
    public GetAccountDetailUC(AccountRepo AR){
        mAR = AR;
    }

    public Account execute(String userId) {
        return mAR.getAccountDetail(userId);
    }
}

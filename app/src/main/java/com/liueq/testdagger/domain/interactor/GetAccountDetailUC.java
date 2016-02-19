package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.StarRepo;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 获取AccountDetail的 UseCase
 *      需要从AccountRepo, StarRepo 获取数据
 */
public class GetAccountDetailUC extends UseCase {

    AccountRepo mAR;
    StarRepo mSR;

    @Inject
    public GetAccountDetailUC(AccountRepo AR, StarRepo SR){
        mAR = AR;
        mSR = SR;
    }

    public Account execute(String userId) {
        Account account = mAR.getAccountDetail(userId);
        account = mSR.getStarStatus(account);
        return account;
    }
}

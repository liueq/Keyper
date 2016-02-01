package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.repository.AccountRepository;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 实际没有使用
 */
public class GetAccountDetailUseCase extends UseCase {

    AccountRepository mAR;

    @Inject
    public GetAccountDetailUseCase(AccountRepository AR){
        mAR = AR;
    }

    public Object execute(String userId) {
        return mAR.getAccountDetail(userId);
    }
}

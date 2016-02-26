package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.StarRepo;
import com.liueq.testdagger.data.repository.TagRepo;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 获取AccountDetail的 UseCase
 *      需要从AccountRepo, StarRepo 获取数据
 */
public class GetAccountDetailUC extends UseCase {

    AccountRepo mAR;
    StarRepo mSR;
    TagRepo mTR;

    @Inject
    public GetAccountDetailUC(AccountRepo AR, StarRepo SR, TagRepo TR){
        mAR = AR;
        mSR = SR;
        mTR = TR;
    }

	/**
     * Get an Account with Star
     * @param userId
     * @return
     */
    public Account execute(String userId) {
        Account account = mAR.getAccountDetail(userId);
        if(account == null){
            return null;
        }else{
            account = mSR.getStarStatus(account);
            return account;
        }
    }

	/**
	 * Get Tag list from an account
     * @param account
     * @return
     */
    public List<Tag> getTagList(Account account){
        return mTR.getTagFromAccount(account);
    }

}

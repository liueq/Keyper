package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.StarRepo;

import javax.inject.Inject;

/**
 * Created by liueq on 19/2/2016.
 * 标星
 */
public class StarUC extends UseCase{

	StarRepo mSR;

	@Inject
	public StarUC(StarRepo mSR) {
		this.mSR = mSR;
	}

	public boolean starAccount(Account account){
		if(account == null)
			return false;
		if(account.id == null){
			//新建Account后，立即标星的情况，立即返回ok，但不对Db进行操作，等到save的时候才更新db
			return true;
		}

		return mSR.starAccount(account);
	}

	public boolean unStarAccount(Account account){
		if(account == null)
			return false;
		if(account.id == null){
			//新建Account后，立即标星的情况，立即返回ok，但不对Db进行操作，等到save的时候才更新db
			return true;
		}

		return mSR.unStarAccount(account);
	}
}

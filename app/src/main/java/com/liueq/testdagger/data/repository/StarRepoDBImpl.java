package com.liueq.testdagger.data.repository;

import com.liueq.testdagger.data.model.Account;

/**
 * Created by liueq on 19/2/2016.
 */
public class StarRepoDBImpl implements StarRepo{

	@Override
	public boolean starAccount(Account account) {
		return false;
	}

	@Override
	public boolean unStarAccount(Account account) {
		return false;
	}
}

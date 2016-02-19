package com.liueq.testdagger.data.repository;

import com.liueq.testdagger.data.model.Account;

import java.util.List;

/**
 * Created by liueq on 19/2/2016.
 * 标星
 */
public interface StarRepo {

	/**
	 * 对Account 进行标星
	 * @param account
	 * @return
	 */
	boolean starAccount(Account account);

	/**
	 * 取消Account 的标星
	 * @param account
	 * @return
	 */
	boolean unStarAccount(Account account);

	/**
	 * 给Account 返回其Star 状态
	 * @param account
	 * @return
	 */
	Account getStarStatus(Account account);

	/**
	 * 给Account List 返回Star 状态
	 * @param list
	 * @return
	 */
	List<Account> getStarStatusList(List<Account> list);

}

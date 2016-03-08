package com.liueq.testdagger.data.repository;

import android.support.annotation.Nullable;

import com.liueq.testdagger.data.model.Account;

import java.util.List;

/**
 * Created by liueq on 27/7/15.
 * Account operation
 */
public interface AccountRepo {

	/**
     * Get all account list from db
     * @return Account list
     */
    List<Account> getAccountList();

	/**
     * Get an Account detail obj
     * @param accountId
     * @return
     */
    Account getAccountDetail(final String accountId);

	/**
	 * Search account with site key
	 * @param key site
	 * @return
	 */
    List<Account> searchAccount(final String key);

	List<Account> searchAccountByField(String key, String field);

	/**
	 * Save an Account list
	 * @param list
	 */
    void saveAccountList(List<Account> list);

	/**
	 * Insert of update account, if id is null, insert.
	 * @param id
	 * @param account
	 * @return
	 */
    String insertOrUpdateAccount(@Nullable  String id, Account account);

	/**
	 * Delete account by id
	 * @param id
	 * @return
	 */
    boolean deleteAccount(String id);
}

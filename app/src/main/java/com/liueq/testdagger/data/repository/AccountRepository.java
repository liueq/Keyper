package com.liueq.testdagger.data.repository;

import com.liueq.testdagger.data.model.Account;

import java.util.List;

/**
 * Created by liueq on 27/7/15.
 */
public interface AccountRepository {

    List<Account> getAccountList();

    Account getAccountDetail(final String accountId);

    List<Account> searchAccount(final String key);
}

package com.liueq.testdagger.data.repository;

import android.support.annotation.Nullable;

import com.liueq.testdagger.data.model.Account;

import java.util.List;

/**
 * Created by liueq on 27/7/15.
 */
public interface AccountRepository {

    List<Account> getAccountList();

    Account getAccountDetail(final String accountId);

    List<Account> searchAccount(final String key);

    void saveAccountList(List<Account> list);

    boolean insertOrUpdateAccount(@Nullable  String id, Account account);

    boolean deleteAccount(String id);
}

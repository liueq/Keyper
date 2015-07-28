package com.liueq.testdagger.domain.interactor;

import android.util.Log;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.utils.Encrypter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 */
public class SaveAccountListUseCase {

    AccountRepositoryImpl mARI;

    @Inject
    public SaveAccountListUseCase(AccountRepositoryImpl ARI){
        mARI = ARI;
    }

    public Object execute(List<Account> list, Account account) {
        if (account != null) {
            boolean insert_flag = true;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(account.id)) {
                    list.set(i, account);
                    insert_flag = false;
                }
            }

            if (insert_flag) {
                list.add(account);
            }

        }

        //加密
        for(Account a : list){
            String password_plaint = a.password;;
            String password_encrypt = Encrypter.encryptByAes(Constants.AES_KEY, password_plaint);
            a.password = password_encrypt;
        }

        mARI.saveAccountList(list);
        return account;
    }
}

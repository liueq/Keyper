package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.utils.Encrypter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 */
public class GetAccountListUseCase extends UseCase {

    AccountRepositoryImpl mARI;

    @Inject
    public GetAccountListUseCase(AccountRepositoryImpl ARI){
        this.mARI = ARI;
    }

    public List execute() {
        List<Account> list = mARI.getAccountList();

        //解密
        for(Account a : list){
            String password_encrypt = a.password;
            String password_plaint = Encrypter.decryptByAes(Constants.AES_KEY, password_encrypt);
            a.password = password_plaint;
        }
        return list;
    }

}

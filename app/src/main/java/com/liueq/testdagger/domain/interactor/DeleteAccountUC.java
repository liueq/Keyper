package com.liueq.testdagger.domain.interactor;

import android.util.Log;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.data.repository.AccountRepositoryDBImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 */
public class DeleteAccountUC extends UseCase {

    AccountRepository mAR;

    @Inject
    public DeleteAccountUC(AccountRepository ar){
        mAR = ar;
    }

    public List<Account> execute(List<Account> list, Account target){
        if(target != null){
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).id.equals(target.id)){
                    list.remove(i);
                }
            }

            return list;
        }

        return null;
    }

    public boolean executeDB(Account account){
        return mAR.deleteAccount(account.id);
    }
}

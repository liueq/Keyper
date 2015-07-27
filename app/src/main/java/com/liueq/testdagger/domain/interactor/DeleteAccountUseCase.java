package com.liueq.testdagger.domain.interactor;

import android.util.Log;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.data.model.Account;

import java.util.List;

/**
 * Created by liueq on 27/7/15.
 */
public class DeleteAccountUseCase extends UseCase {

    public DeleteAccountUseCase(){

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
}

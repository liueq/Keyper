package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.utils.Encrypter;

import javax.inject.Inject;

/**
 * Created by liueq on 30/7/15.
 * Check password UseCase
 */
public class CheckPasswordUC extends UseCase {

    public final static String TAG = "CPUC";

    private SharedPreferenceRepoImpl impl;

    @Inject
    public CheckPasswordUC(SharedPreferenceRepoImpl impl){
        this.impl = impl;
    }

    public boolean execute(String check_need_password){
        String saved_pwd = impl.getProterties(Constants.SP_PWD);
        String check_need_password_md5 = Encrypter.encryptByMD5(check_need_password);

        if(check_need_password_md5.equals(saved_pwd)){
            return true;
        }else{
            return false;
        }
    }
}

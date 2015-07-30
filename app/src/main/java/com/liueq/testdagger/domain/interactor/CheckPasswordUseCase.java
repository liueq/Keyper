package com.liueq.testdagger.domain.interactor;

import android.util.Log;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;
import com.liueq.testdagger.utils.Encrypter;

import javax.inject.Inject;

/**
 * Created by liueq on 30/7/15.
 */
public class CheckPasswordUseCase extends UseCase {

    public final static String TAG = "CPUC";

    private SharedPreferenceRepositoryImpl impl;

    @Inject
    public CheckPasswordUseCase(SharedPreferenceRepositoryImpl impl){
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

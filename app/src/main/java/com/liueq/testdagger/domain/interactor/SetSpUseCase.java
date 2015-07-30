package com.liueq.testdagger.domain.interactor;

import android.text.TextUtils;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;
import com.liueq.testdagger.utils.Encrypter;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by liueq on 29/7/15.
 */
public class SetSpUseCase {

    private SharedPreferenceRepositoryImpl impl;

    @Inject
    public SetSpUseCase(SharedPreferenceRepositoryImpl impl){
        this.impl = impl;
    }

    public void savePwdEncStatus(boolean encrypt){
        HashMap<String, String> map = new HashMap<>();
        if(encrypt){
            map.put(Constants.SP_IS_PWD_ENC, Constants.YES);
        }else{
            map.put(Constants.SP_IS_PWD_ENC, Constants.NO);
        }
        impl.saveProperties(map);
    }

    public void saveDescEncStatus(boolean encrypt){
        HashMap<String, String> map = new HashMap<>();
        if(encrypt){
            map.put(Constants.SP_IS_DESC_ENC, Constants.YES);
        }else{
            map.put(Constants.SP_IS_DESC_ENC, Constants.NO);
        }
        impl.saveProperties(map);
    }

    public boolean savePassword(String password){
        HashMap<String, String> map = new HashMap<>();
        if(TextUtils.isEmpty(password)){
            return false;
        }
        String password_md5 = Encrypter.encryptByMD5(password);
        map.put(Constants.SP_PWD, password_md5);
        impl.saveProperties(map);
        return true;
    }
}

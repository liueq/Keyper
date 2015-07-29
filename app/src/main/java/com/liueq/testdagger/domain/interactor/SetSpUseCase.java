package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;

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
}

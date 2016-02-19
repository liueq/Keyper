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
public class SetSpUC {

    private SharedPreferenceRepositoryImpl impl;

    @Inject
    public SetSpUC(SharedPreferenceRepositoryImpl impl){
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

    public boolean saveAES(String aes_input){
        HashMap<String, String> map = new HashMap<>();
        if(TextUtils.isEmpty(aes_input)){
            return false;
        }

        //补齐16位
        if(aes_input.length() != 16){
            int length = aes_input.length();
            int diff = 16 - length;
            StringBuffer sb = new StringBuffer();
            sb.append(aes_input);
            for(int i = 0; i < diff; i++){
                sb.append("*");
            }
            aes_input = sb.toString();
        }

        map.put(Constants.SP_AES, aes_input);
        impl.saveProperties(map);
        return true;
    }

    public boolean saveFilePathState(HashMap<String, Boolean> state){
        int both_uncheck_flag = 0;
        HashMap<String, String> map = new HashMap<>();
        if(state.get(Constants.SP_IS_SAVE_EXTERNAL)){
            map.put(Constants.SP_IS_SAVE_EXTERNAL, Constants.YES);
        }else{
            map.put(Constants.SP_IS_SAVE_EXTERNAL, Constants.NO);
            both_uncheck_flag++;
        }

        if(state.get(Constants.SP_IS_SAVE_INTERNAL)){
            map.put(Constants.SP_IS_SAVE_INTERNAL, Constants.YES);
        }else{
            map.put(Constants.SP_IS_SAVE_INTERNAL, Constants.NO);
            both_uncheck_flag++;
        }

        if(both_uncheck_flag == 2){
            return false;
        }

        impl.saveProperties(map);
        return true;
    }
}

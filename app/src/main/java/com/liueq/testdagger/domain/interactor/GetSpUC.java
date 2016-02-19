package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by liueq on 29/7/15.
 */
public class GetSpUC extends UseCase {

    private SharedPreferenceRepoImpl impl;

    @Inject
    public GetSpUC(SharedPreferenceRepoImpl impl){
        this.impl = impl;
    }

    public HashMap<String, String> getEncStatus(){
        HashMap<String, String> map = new HashMap<>();
        String pwd_enc_status = impl.getProterties(Constants.SP_IS_PWD_ENC);
        String desc_enc_status = impl.getProterties(Constants.SP_IS_DESC_ENC);

        map.put(Constants.SP_IS_PWD_ENC, pwd_enc_status);
        map.put(Constants.SP_IS_DESC_ENC, desc_enc_status);

        return map;
    }

    public HashMap<String, String> getAESPassword(){
        HashMap<String, String> map = new HashMap<>();
        String aes_pwd = impl.getProterties(Constants.SP_AES);

        map.put(Constants.SP_AES, aes_pwd);
        return map;
    }

    public HashMap<String, Boolean> getFileSavePath(){
        HashMap<String, Boolean> map = new HashMap<>();
        if(impl.getProterties(Constants.SP_IS_SAVE_EXTERNAL) != null && impl.getProterties(Constants.SP_IS_SAVE_EXTERNAL).equals(Constants.NO)){
            map.put(Constants.SP_IS_SAVE_EXTERNAL, false);
        }else{
            map.put(Constants.SP_IS_SAVE_EXTERNAL, true);
        }

        if(impl.getProterties(Constants.SP_IS_SAVE_INTERNAL) != null && impl.getProterties(Constants.SP_IS_SAVE_INTERNAL).equals(Constants.NO)){
            map.put(Constants.SP_IS_SAVE_INTERNAL, false);
        }else{
            map.put(Constants.SP_IS_SAVE_INTERNAL, true);
        }

        return map;
    }

}

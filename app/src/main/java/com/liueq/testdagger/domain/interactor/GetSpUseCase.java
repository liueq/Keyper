package com.liueq.testdagger.domain.interactor;

import android.content.Context;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by liueq on 29/7/15.
 */
public class GetSpUseCase extends UseCase {

    private SharedPreferenceRepositoryImpl impl;

    @Inject
    public GetSpUseCase(SharedPreferenceRepositoryImpl impl){
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

    public HashMap<String, String> getFileSavePath(){
        HashMap<String, String> map = new HashMap<>();
        String file_path = impl.getProterties(Constants.STORAGE_PATH);

        map.put(Constants.STORAGE_PATH, file_path);
        return map;
    }

}

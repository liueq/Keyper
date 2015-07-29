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

}

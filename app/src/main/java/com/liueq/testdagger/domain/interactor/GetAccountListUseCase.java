package com.liueq.testdagger.domain.interactor;

import android.util.Log;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.utils.Encrypter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 */
public class GetAccountListUseCase extends UseCase {

    AccountRepositoryImpl mARI;
    GetSpUseCase mGetSpUseCase;
    public final static String TAG = "GetALUS";

    @Inject
    public GetAccountListUseCase(AccountRepositoryImpl ARI, GetSpUseCase getSpUseCase){
        this.mARI = ARI;
        this.mGetSpUseCase = getSpUseCase;
    }

    public List execute() {
        List<Account> list = mARI.getAccountList();

        //获取AES密钥
        HashMap<String, String> map = mGetSpUseCase.getAESPassword();
        String aes_key = map.get(Constants.SP_AES);

        //选择解密字段
        HashMap<String, String> map_enc_field = mGetSpUseCase.getEncStatus();

        //解密
        for(Account a : list){
            String password_encrypt = a.password;
            String desc_encrypt = a.description;

            if(!map_enc_field.get(Constants.SP_IS_PWD_ENC).equals(Constants.NO)){
                String password_plaint = Encrypter.decryptByAes(aes_key, password_encrypt);
                a.password = password_plaint;
            }

            if(!map_enc_field.get(Constants.SP_IS_DESC_ENC).equals(Constants.NO)){
                String desc_plaint = Encrypter.decryptByAes(aes_key, desc_encrypt);
                a.description = desc_plaint;
            }


        }
        return list;
    }

}

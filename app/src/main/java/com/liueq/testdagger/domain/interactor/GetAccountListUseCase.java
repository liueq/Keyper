package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.utils.Encrypter;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 */
public class GetAccountListUseCase extends UseCase {

    AccountRepository mAR;
    GetSpUseCase mGetSpUseCase;
    public final static String TAG = "GetALUS";

    @Inject
    public GetAccountListUseCase(AccountRepository AR, GetSpUseCase getSpUseCase){
        this.mAR = AR;
        this.mGetSpUseCase = getSpUseCase;
    }

    public List execute() {
        List<Account> list = mAR.getAccountList();

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

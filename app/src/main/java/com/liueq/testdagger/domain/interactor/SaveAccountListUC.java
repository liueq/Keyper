package com.liueq.testdagger.domain.interactor;

import android.util.Log;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.utils.Encrypter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 */
public class SaveAccountListUC {

    AccountRepository mAR;
    GetSpUC mGetSpUC;
    public final static String TAG = "SaveAlus";

    @Inject
    public SaveAccountListUC(AccountRepository AR, GetSpUC getSpUC){
        mAR = AR;
        mGetSpUC = getSpUC;
    }

    public Object execute(List<Account> mList, Account account) {
        List<Account> list = new ArrayList<>();//如果不新建一个list，那么加密的修改会影响到mList
        for(Account a : mList){
            Account tmp = null;
            try {
                tmp = a.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if(tmp != null){
                list.add(tmp);
            }
        }

        if (account != null) {
            boolean insert_flag = true;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(account.id)) {
                    list.set(i, account);
                    insert_flag = false;
                }
            }

            if (insert_flag) {
                list.add(account);
            }

        }

        //从SP获取AES密钥
        HashMap<String, String> map_aes = mGetSpUC.getAESPassword();
        String aes_key = map_aes.get(Constants.SP_AES);

        //选择加密字段
        HashMap<String, String> map_enc_field = mGetSpUC.getEncStatus();

        //加密
        for(Account a : list){
            String password_plaint = a.password;
            String desc_plaint = a.description;

            if(!map_enc_field.get(Constants.SP_IS_PWD_ENC).equals(Constants.NO)){
                Log.d(TAG, "execute do encrypt password");
                String password_encrypt = Encrypter.encryptByAes(aes_key, password_plaint);
                a.password = password_encrypt;
            }

            if(!map_enc_field.get(Constants.SP_IS_DESC_ENC).equals(Constants.NO)){
                Log.d(TAG, "execute do encrypt description");
                String desc_encrypt = Encrypter.encryptByAes(aes_key, desc_plaint);
                a.description = desc_encrypt;
            }

        }

        mAR.saveAccountList(list);
        return account;
    }

    public boolean executeDB(Account account) {
        return mAR.insertOrUpdateAccount(account.id, account);
    }
}

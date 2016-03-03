package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.StarRepo;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 获取AccountList的UseCase
 *      包括从AccountRepo, StarRepo 获取数据
 */
public class GetAccountListUC extends UseCase {

    AccountRepo mAR;
    StarRepo mSR;

    SharedPUC mSharedPUC;

    public final static String TAG = "GetALUS";

    @Inject
    public GetAccountListUC(AccountRepo AR, StarRepo SR, SharedPUC sharedPUC){
        this.mAR = AR;
        this.mSR = SR;
        this.mSharedPUC = sharedPUC;
    }

//    public List execute() {
//        List<Account> list = mAR.getAccountList();
//
//        //获取AES密钥
//        HashMap<String, String> map = mSharedPUC.getDBPassword();
//        String aes_key = map.get(Constants.SP_AES);
//
//        //选择解密字段
//        HashMap<String, String> map_enc_field = mSharedPUC.getEncStatus();
//
//        //解密
//        for(Account a : list){
//            String password_encrypt = a.password;
//            String desc_encrypt = a.description;
//
//            if(!map_enc_field.get(Constants.SP_IS_PWD_ENC).equals(Constants.NO)){
//                String password_plaint = Encrypter.decryptByAes(aes_key, password_encrypt);
//                a.password = password_plaint;
//            }
//
//            if(!map_enc_field.get(Constants.SP_IS_DESC_ENC).equals(Constants.NO)){
//                String desc_plaint = Encrypter.decryptByAes(aes_key, desc_encrypt);
//                a.description = desc_plaint;
//            }
//
//
//        }
//        return list;
//    }

    public List<Account> executeDB(){
        List<Account> list = mAR.getAccountList();
        list = mSR.getStarStatusList(list);
        return list;
    }

}

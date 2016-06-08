package com.liueq.keyper.domain.interactor;

import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.data.repository.AccountRepo;
import com.liueq.keyper.data.repository.StarRepo;
import com.liueq.keyper.data.repository.TagRepo;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 保存
 */
public class SaveAccountListUC {

    AccountRepo mAR;
    StarRepo mSR;
    TagRepo mTR;

    SharedPUC mSharedPUC;

    public final static String TAG = "SaveAlus";

    @Inject
    public SaveAccountListUC(AccountRepo AR, SharedPUC sharedPUC, StarRepo SR, TagRepo TR){
        mAR = AR;
        mSR = SR;
        mTR = TR;
        mSharedPUC = sharedPUC;
    }

//    public Object execute(List<Account> mList, Account account) {
//        List<Account> list = new ArrayList<>();//如果不新建一个list，那么加密的修改会影响到mList
//        for(Account a : mList){
//            Account tmp = null;
//            try {
//                tmp = a.clone();
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//
//            if(tmp != null){
//                list.add(tmp);
//            }
//        }
//
//        if (account != null) {
//            boolean insert_flag = true;
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).id.equals(account.id)) {
//                    list.set(i, account);
//                    insert_flag = false;
//                }
//            }
//
//            if (insert_flag) {
//                list.add(account);
//            }
//
//        }
//
//        //从SP获取AES密钥
//        HashMap<String, String> map_aes = mSharedPUC.getDBPassword();
//        String aes_key = map_aes.get(Constants.SP_AES);
//
//        //选择加密字段
//
//        //加密
//        for(Account a : list){
//            String password_plaint = a.password;
//            String desc_plaint = a.description;
//
//            if(!map_enc_field.get(Constants.SP_IS_PWD_ENC).equals(Constants.NO)){
//                Log.d(TAG, "execute do encrypt password");
//                String password_encrypt = Encrypter.encryptByAes(aes_key, password_plaint);
//                a.password = password_encrypt;
//            }
//
//            if(!map_enc_field.get(Constants.SP_IS_DESC_ENC).equals(Constants.NO)){
//                Log.d(TAG, "execute do encrypt description");
//                String desc_encrypt = Encrypter.encryptByAes(aes_key, desc_plaint);
//                a.description = desc_encrypt;
//            }
//
//        }
//
//        mAR.saveAccountList(list);
//        return account;
//    }

    public String executeDB(Account account) {
        String result_id = null;
        result_id = mAR.insertOrUpdateAccount(account.id, account);
        account.id = result_id;
        if(account.is_stared){
            mSR.starAccount(account); //(已解决)如果是一个新的account，那么这里不能返回account生成的id，会报错。
        }else{
            mSR.unStarAccount(account);
        }

        //更新TagAndPassword tab 数据
        mTR.replaceAccountTag(account, account.tag_list);
        return result_id;
    }
}

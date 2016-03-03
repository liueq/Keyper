package com.liueq.testdagger.domain.interactor;

import android.text.TextUtils;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.utils.Encrypter;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by liueq on 29/7/15.
 * 所有对于SharedPreference的操作
 */
public class SharedPUC extends UseCase {

    private SharedPreferenceRepoImpl impl;

    @Inject
    public SharedPUC(SharedPreferenceRepoImpl impl){
        this.impl = impl;
    }

	/**
     * 获得DB的密码
     * @return
     */
    public HashMap<String, String> getDBPassword(){
        HashMap<String, String> map = new HashMap<>();
        String db_pwd = impl.getProterties(Constants.SP_DB_PWD);

        map.put(Constants.SP_DB_PWD, db_pwd);
        return map;
    }

    /**
     * 保存登陆密码
     * @param password
     * @return
     */
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

    /**
     * 保存AES密码
     * @param db_password
     * @return
     */
    public boolean saveDBPassword(String db_password){
        HashMap<String, String> map = new HashMap<>();
        if(TextUtils.isEmpty(db_password)){
            return false;
        }

        map.put(Constants.SP_DB_PWD, db_password);
        impl.saveProperties(map);
        return true;
    }

	/**
     * Check login password
     * @param check_need_password
     * @return
     */
    public boolean checkPassword(String check_need_password){
        String saved_pwd = impl.getProterties(Constants.SP_PWD);
        String check_need_password_md5 = Encrypter.encryptByMD5(check_need_password);

        if(check_need_password_md5.equals(saved_pwd)){
            return true;
        }else{
            return false;
        }
    }

}

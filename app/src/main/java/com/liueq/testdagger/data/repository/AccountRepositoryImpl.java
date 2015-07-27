package com.liueq.testdagger.data.repository;

import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.utils.Encrypter;
import com.liueq.testdagger.utils.FileReader;
import com.liueq.testdagger.utils.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 27/7/15.
 */
public class AccountRepositoryImpl implements AccountRepository{

    public final static String TAG = "ARI";

    FileReader mFileReader;
    List<Account> mAccountList;
    List<Account> mFilteredList;

    public AccountRepositoryImpl(FileReader fileReader){
        mFileReader = fileReader;
    }

    @Override
    public List<Account> getAccountList() {
        JsonReader json = mFileReader.retrieveData();
        if(mAccountList != null){
            mAccountList.clear();
        }else{
            mAccountList = new ArrayList<Account>();
        }

        mAccountList.addAll(JsonParser.jsonToObj(json));    //由于TypeToken的原因，无法使用泛型

        if(BuildConfig.DEBUG){
            for(Account i : mAccountList){
                Log.i(TAG, "loadData id " + i.id);
                Log.i(TAG, "loadData site" + i.site);
                Log.i(TAG, "loadData userName" + i.userName);
                Log.i(TAG, "loadData password" + i.password);
                Log.i(TAG, "loadData mail" + i.mail);
                Log.i(TAG, "loadData description" + i.description);

            }
        }

        //解密
        for(Account a : mAccountList){
            String password_encrypt = a.password;
            String password_plaint = Encrypter.decryptByAes(Constants.AES_KEY, password_encrypt);
            a.password = password_plaint;
        }
        return mAccountList;
    }

    @Override
    public Account getAccountDetail(String accountId) {
        return null;
    }

    @Override
    public List<Account> searchAccount(String key) {
        if(mFilteredList == null){
            mFilteredList = new ArrayList<Account>();
        }else{
            mFilteredList.clear();
        }

        if(mAccountList == null){
            getAccountList();
        }

        for(Account a : mAccountList){
            String site_lower = a.site.toLowerCase();       //无视大小写
            if(site_lower.contains(key.toLowerCase())){
                mFilteredList.add(a);
            }
        }
        return mFilteredList;
    }

    @Override
    public void saveAccountList(List<Account> accountList) {

        //加密
        for(Account a : accountList){
            String password_plaint = a.password;;
            String password_encrypt = Encrypter.encryptByAes(Constants.AES_KEY, password_plaint);
            a.password = password_encrypt;

            if(BuildConfig.DEBUG){
                Log.d(TAG, "saveData after encrypt " + a.password);
            }
        }

        mFileReader.presistData(JsonParser.objToJson(accountList));
    }
}

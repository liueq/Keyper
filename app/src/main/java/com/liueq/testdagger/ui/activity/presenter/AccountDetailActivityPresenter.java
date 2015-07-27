package com.liueq.testdagger.ui.activity.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.utils.Encrypter;
import com.liueq.testdagger.utils.FileReader;
import com.liueq.testdagger.utils.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 13/7/15.
 */
public class AccountDetailActivityPresenter {

    public final static String TAG = "AccountDAPresenter";

    private AccountDetailActivity activity;
    FileReader mFileReader;
    List<Account> mAccountList;
    private Account mCurrentAccount;

    public AccountDetailActivityPresenter(AccountDetailActivity activity, FileReader fileReader, List<Account> accountList){
        this.activity = activity;
        this.mFileReader = fileReader;
        this.mAccountList = accountList;
    }

    public void loadData(Bundle bundle){
        mCurrentAccount = (Account) bundle.getSerializable("account");
        activity.updateUI(mCurrentAccount);

    }

    /**
     * 包括更新和插入
     * @param account
     * @return
     */
    public boolean saveData(Account account){
        boolean insert_flag = true;
        for(int i = 0; i < mAccountList.size(); i++){
            if(mAccountList.get(i).id.equals(account.id)){
                mAccountList.set(i, account);
                insert_flag = false;
            }
        }

        if(insert_flag){
            mAccountList.add(account);
            mCurrentAccount = account;
        }

//        SaveDataTask task = new SaveDataTask(activity);
//        task.execute(mAccountList, mFileReader);

        //加密
        for(Account a : mAccountList){
            String password_plaint = a.password;;
            String password_encrypt = Encrypter.encryptByAes(Constants.AES_KEY, password_plaint);
            a.password = password_encrypt;

            if(BuildConfig.DEBUG){
                Log.d(TAG, "saveData after encrypt " + a.password);
            }
        }

        mFileReader.presistData(JsonParser.objToJson(mAccountList));

        loadData();
        return true;
    }

    public Account getCurrentAccount(){
        return mCurrentAccount;
    }

    public void deleteAccount(){
        if(mCurrentAccount != null){
            for(int i = 0; i < mAccountList.size(); i++){
                if(mAccountList.get(i).id.equals(mCurrentAccount.id)){
                    mAccountList.remove(i);
                }
            }

            if(BuildConfig.DEBUG){
                Log.d(TAG, "deleteAccount mCurrentAccount is " + mCurrentAccount);
                Log.d(TAG, "deleteAccount mAccountList is " + mAccountList);
            }

            //加密
            for(Account a : mAccountList){
                String password_plaint = a.password;;
                String password_encrypt = Encrypter.encryptByAes(Constants.AES_KEY, password_plaint);
                a.password = password_encrypt;
            }

            mFileReader.presistData(JsonParser.objToJson(mAccountList));

            loadData();
        }
    }


    public void loadData(){
        JsonReader json = mFileReader.retrieveData();
        mAccountList.clear();
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
    }
}

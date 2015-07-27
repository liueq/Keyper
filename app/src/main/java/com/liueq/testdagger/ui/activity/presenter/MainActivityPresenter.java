package com.liueq.testdagger.ui.activity.presenter;

import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.utils.Encrypter;
import com.liueq.testdagger.utils.FileReader;
import com.liueq.testdagger.utils.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

/**
 * Created by liueq on 13/7/15.
 */
public class MainActivityPresenter {

    public final static String TAG = "MainActivityPresenter";

    FileReader mFileReader;
    private MainActivity mainActivity;
    List<Account> mAccountList;
    List<Account> mFilteredList;

    public MainActivityPresenter(MainActivity mainActivity, FileReader fileReader, List<Account> list) {
        this.mainActivity = mainActivity;
        this.mFileReader = fileReader;
        this.mAccountList = list;
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

        mainActivity.updateUI(mAccountList);
    }

    public void search(String searchKey){
        if(mFilteredList == null){
            mFilteredList = new ArrayList<Account>();
        }else{
            mFilteredList.clear();
        }
        for(Account a : mAccountList){
            String site_lower = a.site.toLowerCase();       //无视大小写
            if(site_lower.contains(searchKey.toLowerCase())){
                mFilteredList.add(a);
            }
        }

        mainActivity.updateUI(mFilteredList);
    }

}

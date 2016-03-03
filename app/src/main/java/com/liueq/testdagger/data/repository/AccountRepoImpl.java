package com.liueq.testdagger.data.repository;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.SharedPUC;
import com.liueq.testdagger.utils.FileReader;
import com.liueq.testdagger.utils.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liueq on 27/7/15.
 * 旧版本的JSON实现，现在已经不再使用了
 */
public class AccountRepoImpl{
//
//    public final static String TAG = "ARI";
//
//    FileReader mFileReader;
//    List<Account> mAccountList;
//    List<Account> mFilteredList;
//    SharedPUC mSharedPUC;
//
//    public AccountRepoImpl(FileReader fileReader, SharedPUC sharedPUC) {
//        mFileReader = fileReader;
//        mSharedPUC = sharedPUC;
//    }
//
//    @Override
//    public List<Account> getAccountList() {
//
//        List<Account> list_internal = null;
//        List<Account> list_external = null;
//
//
//        //文件位置选择
//        HashMap<String, Boolean> map = mSharedPUC.getFileSavePath();
//
//        if (map.get(Constants.SP_IS_SAVE_INTERNAL)) {
//            Log.d(TAG, "getAccountList from INTERNAL");
//            mFileReader.setmFilePath(Constants.INTERNAL_STORAGE_PATH);
//            list_internal = getListFrom();
//
//        } else if (map.get(Constants.SP_IS_SAVE_EXTERNAL)) {
//            Log.d(TAG, "getAccountList from EXTERNAL");
//            mFileReader.setmFilePath(Constants.EXTERNAL_STORAGE_PATH);
//            list_external = getListFrom();
//
//        }
//
//        if (mAccountList != null) {
//            mAccountList.clear();
//        } else {
//            mAccountList = new ArrayList<Account>();
//        }
//
//        if (list_external != null) {
//            mAccountList.addAll(list_external);
//        }
//
//        if (list_internal != null) {
//            mAccountList.addAll(list_internal);
//        }
//
//        return mAccountList;
//    }
//
//    private List<Account> getListFrom() {
//        List<Account> list = new ArrayList<>();
//
//        JsonReader json = mFileReader.retrieveData();
//        if (list != null) {
//            list.clear();
//        } else {
//            list = new ArrayList<Account>();
//        }
//
//        list.addAll(JsonParser.jsonToObj(json));    //由于TypeToken的原因，无法使用泛型
//
//        if (BuildConfig.DEBUG) {
//            for (Account i : list) {
//                Log.i(TAG, "loadDataFromDB id " + i.id);
//                Log.i(TAG, "loadDataFromDB site" + i.site);
//                Log.i(TAG, "loadDataFromDB username" + i.username);
//                Log.i(TAG, "loadDataFromDB password" + i.password);
//                Log.i(TAG, "loadDataFromDB mail" + i.mail);
//                Log.i(TAG, "loadDataFromDB description" + i.description);
//
//            }
//        }
//
//        return list;
//    }
//
//    @Override
//    public Account getAccountDetail(String accountId) {
//        return null;
//    }
//
//    @Override
//    public List<Account> searchAccount(String key) {
//        if (mFilteredList == null) {
//            mFilteredList = new ArrayList<Account>();
//        } else {
//            mFilteredList.clear();
//        }
//
//        if (mAccountList == null) {
//            getAccountList();
//        }
//
//        for (Account a : mAccountList) {
//            String site_lower = a.site.toLowerCase();       //无视大小写
//            if (site_lower.contains(key.toLowerCase())) {
//                mFilteredList.add(a);
//            }
//        }
//        return mFilteredList;
//    }
//
//    @Override
//    public void saveAccountList(List<Account> accountList) {
//        //增加选择保存文件位置
//        HashMap<String, Boolean> map = mSharedPUC.getFileSavePath();
//
//        if (map.get(Constants.SP_IS_SAVE_EXTERNAL)) {
//            mFileReader.setmFilePath(Constants.EXTERNAL_STORAGE_PATH);
//            mFileReader.presistData(JsonParser.objToJson(accountList));
//
//        } else {
//            File file = new File(Constants.EXTERNAL_STORAGE_PATH, Constants.STORAGE_FILE);
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//
//
//        if (map.get(Constants.SP_IS_SAVE_INTERNAL)) {
//            mFileReader.setmFilePath(Constants.INTERNAL_STORAGE_PATH);
//            mFileReader.presistData(JsonParser.objToJson(accountList));
//        } else {
//            File file = new File(Constants.INTERNAL_STORAGE_PATH, Constants.STORAGE_FILE);
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//
//    }
//
//    @Override
//    public String insertOrUpdateAccount(@Nullable String id, Account account) {
//        return null;
//    }
//
//    @Override
//    public boolean deleteAccount(String id) {
//        return false;
//    }
}

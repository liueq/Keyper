package com.liueq.keyper.data.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.liueq.keyper.Constants;

/**
 * Created by liueq on 21/7/15.
 * Settings mode (not use)
 */
public class Settings {

    private String mLoginPwd;//登陆密码
    private String mAESKey;//AES加密密码
    private SharedPreferences mSP;

    public Settings(Context context){
        mSP = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
    }

    public String getmLoginPwd() {
        return mLoginPwd = mSP.getString(Constants.SP_PWD, "");
    }

    public void setmLoginPwd(String mLoginPwd) {
        this.mLoginPwd = mLoginPwd;
        mSP.edit().putString(Constants.SP_PWD, mLoginPwd).commit();
    }

    public String getmAESKey() {
        return mAESKey = mSP.getString(Constants.AES_KEY, "");
    }

    public void setmAESKey(String mAESKey) {
        this.mAESKey = mAESKey;
        mSP.edit().putString(Constants.AES_KEY, mAESKey).commit();
    }

    @Override
    public String toString() {
        return "Settings{" +
                "mLoginPwd='" + mLoginPwd + '\'' +
                ", mAESKey='" + mAESKey + '\'' +
                '}';
    }
}

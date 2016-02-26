package com.liueq.testdagger.ui.launch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.ui.main.MainActivity;
import com.liueq.testdagger.utils.Encrypter;

/**
 * Created by liueq on 16/7/15.
 */
public class SplashActivityPresenter extends Presenter {

    public final static String TAG = "Splash P";

    public boolean hasPassword = false;

    private SplashActivity activity;

    public SplashActivityPresenter(SplashActivity activity){
        this.activity = activity;
    }

    public void login(String pwd1, String pwd2){
        SharedPreferences sp = activity.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        String saved_pwd = sp.getString(Constants.SP_PWD, null);

        String pwd_1_md5 = Encrypter.encryptByMD5(pwd1);
        String pwd_2_md5 = Encrypter.encryptByMD5(pwd2);

        if(hasPassword){
            if(pwd_1_md5.equals(saved_pwd)){
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast.makeText(activity, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(pwd_1_md5.equals(pwd_2_md5)){
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.SP_PWD, pwd_1_md5);
                editor.commit();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast.makeText(activity, "Password Mismatch", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
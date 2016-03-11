package com.liueq.testdagger.ui.launch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.ui.main.MainActivity;
import com.liueq.testdagger.utils.Encrypter;

/**
 * Created by liueq on 16/7/15.
 * Presenter of SplashActivity
 */
public class SplashActivityPresenter extends Presenter {

    public final static String TAG = "Splash P";

    public boolean hasPassword = false;

    public String mMode;

    public final static String MODE_LAUNCH = "mode_launch";
    public final static String MODE_LOCK = "mode_lock";

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
                //Save as DB password in memory
                TestApplication.setDBPassword(pwd1);

                if(mMode.equals(MODE_LAUNCH)){
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }else if(mMode.equals(MODE_LOCK)){
                    activity.finish();
                }
            }else{
                Toast.makeText(activity, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(pwd_1_md5.equals(pwd_2_md5)){
                //Save as DB password
                TestApplication.setDBPassword(pwd1);

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

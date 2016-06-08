package com.liueq.keyper;

import android.app.Application;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by liueq on 13/7/15.
 * Application File
 */
public class TestApplication extends Application{

    private AppComponent appComponent;

    private static TestApplication mInstance;

    private static String mDBPassword;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //Dagger2 init.
        appComponent = DaggerAppComponent.builder()
                            .appModule(new AppModule(this))
                            .build();//注意这里的list，之后在listPresenter和detailPresenter中都注入了此对象，必须保持同步

        //Sqlcipher init.
        SQLiteDatabase.loadLibs(this);
    }

    public static TestApplication getApplication(){
        return mInstance;
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }


    public static String getDBPassword(){
        return mDBPassword;
    }

    public static void setDBPassword(String password){
        mDBPassword = password;
    }
}

package com.liueq.testdagger;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.data.model.Account;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by liueq on 13/7/15.
 * Application File
 */
public class TestApplication extends Application{

    private AppComponent appComponent;

    private static TestApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Constants.INTERNAL_STORAGE_PATH = getFilesDir().toString();

        //Dagger2 init.
        appComponent = DaggerAppComponent.builder()
                            .appModule(new AppModule(this, new ArrayList<Account>())) //TODO 此处的Account list 是否还需要？？
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

}

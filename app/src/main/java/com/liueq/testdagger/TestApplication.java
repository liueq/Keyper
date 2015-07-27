package com.liueq.testdagger;

import android.app.Application;
import android.content.Context;

import com.liueq.testdagger.data.model.Account;

import java.util.ArrayList;

/**
 * Created by liueq on 13/7/15.
 */
public class TestApplication extends Application{

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this, new ArrayList<Account>())).build();
    }

    public static TestApplication get(Context context){
        return (TestApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}

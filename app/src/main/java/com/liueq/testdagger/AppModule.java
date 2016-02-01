package com.liueq.testdagger;

import android.app.Application;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.utils.FileReader;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 13/7/15.
 * 提供所有的SingleTon依赖
 */
@Module
public class AppModule {
    private Application application;
    private List<Account> accountList;

    public AppModule(Application application, List<Account> accountList){
        this.application = application;
        this.accountList = accountList;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    public FileReader provideFileReader(){
        return FileReader.getInstance();
    }

    @Provides
    @Singleton
    public List<Account> provideAccountList(){
        return accountList;
    }

}

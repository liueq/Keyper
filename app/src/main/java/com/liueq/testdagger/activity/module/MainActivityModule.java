package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.activity.presenter.MainActivityPresenter;
import com.liueq.testdagger.utils.FileReader;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 13/7/15.
 */
@Module
public class MainActivityModule {

    private MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivity provideMainActivity(){
        return mainActivity;
    }

    @Provides
    @ActivityScope
    MainActivityPresenter provideMainActivityPresenter(FileReader fileReader, List<Account> accountList){
        //这里的FileReader是由AppModule中提供，不需要显示注入
        //然后从这里向Presenter中提供的时候，需要使用构造方法传入，不能在Presenter中直接@Inject注入
        return new MainActivityPresenter(mainActivity, fileReader, accountList);
    }
}

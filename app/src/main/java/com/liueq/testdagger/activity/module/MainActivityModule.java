package com.liueq.testdagger.activity.module;

import com.liueq.testdagger.activity.ActivityScope;
import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepository;
import com.liueq.testdagger.data.repository.SharedPreferenceRepositoryImpl;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUseCase;
import com.liueq.testdagger.domain.interactor.GetAccountListUseCase;
import com.liueq.testdagger.domain.interactor.GetSpUseCase;
import com.liueq.testdagger.domain.interactor.SearchAccountUseCase;
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
    MainActivityPresenter provideMainActivityPresenter(List<Account> accountList, FileReader fileReader){
        //这里的FileReader是由AppModule中提供，不需要显示注入
        //然后从这里向Presenter中提供的时候，需要使用构造方法传入，不能在Presenter中直接@Inject注入

        AccountRepository ar = new AccountRepositoryImpl(fileReader);
        SharedPreferenceRepository spr = new SharedPreferenceRepositoryImpl(mainActivity);

        GetSpUseCase getSpUseCase = new GetSpUseCase((SharedPreferenceRepositoryImpl) spr);
        GetAccountListUseCase getAccountListUseCase = new GetAccountListUseCase((AccountRepositoryImpl) ar, getSpUseCase);
        SearchAccountUseCase searchAccountUseCase = new SearchAccountUseCase((AccountRepositoryImpl) ar);

        return new MainActivityPresenter(mainActivity, accountList, getAccountListUseCase, searchAccountUseCase);
    }
}

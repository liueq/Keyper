package com.liueq.testdagger.ui.main;

import com.liueq.testdagger.base.ActivityScope;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.AccountRepoDBImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepo;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.data.repository.StarRepo;
import com.liueq.testdagger.data.repository.StarRepoDBImpl;
import com.liueq.testdagger.data.repository.TagRepo;
import com.liueq.testdagger.data.repository.TagRepoDBImpl;
import com.liueq.testdagger.domain.interactor.AddTagUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.GetSpUC;
import com.liueq.testdagger.domain.interactor.GetStarListUC;
import com.liueq.testdagger.domain.interactor.SearchAccountUC;
import com.liueq.testdagger.domain.interactor.StarUC;
import com.liueq.testdagger.utils.FileReader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 13/7/15.
 * Main Activity Module
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
    MainActivityPresenter provideMainActivityPresenter(FileReader fileReader){
        //这里的FileReader是由AppModule中提供，不需要显示注入
        //然后从这里向Presenter中提供的时候，需要使用构造方法传入，不能在Presenter中直接@Inject注入

        SharedPreferenceRepo spr = new SharedPreferenceRepoImpl(mainActivity);
        GetSpUC getSpUC = new GetSpUC((SharedPreferenceRepoImpl) spr);

        //Repository
        AccountRepo ar = new AccountRepoDBImpl(mainActivity, getSpUC);
        StarRepo sr = new StarRepoDBImpl(mainActivity);
        TagRepo tr = new TagRepoDBImpl(mainActivity);

        //UseCase
        GetAccountListUC getAccountListUC = new GetAccountListUC(ar, sr, getSpUC);
        SearchAccountUC searchAccountUC = new SearchAccountUC(ar, sr);
        StarUC starUC = new StarUC(sr);
        GetStarListUC getStarListUC = new GetStarListUC(ar, sr, getSpUC);
        AddTagUC addTagUC = new AddTagUC(tr);

        return new MainActivityPresenter(mainActivity, getAccountListUC, searchAccountUC, starUC, getStarListUC, addTagUC);
    }
}

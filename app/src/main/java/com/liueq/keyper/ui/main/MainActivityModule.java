package com.liueq.keyper.ui.main;

import com.liueq.keyper.base.ActivityScope;
import com.liueq.keyper.data.repository.AccountRepo;
import com.liueq.keyper.data.repository.AccountRepoDBImpl;
import com.liueq.keyper.data.repository.SharedPreferenceRepo;
import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;
import com.liueq.keyper.data.repository.StarRepo;
import com.liueq.keyper.data.repository.StarRepoDBImpl;
import com.liueq.keyper.data.repository.TagRepo;
import com.liueq.keyper.data.repository.TagRepoDBImpl;
import com.liueq.keyper.domain.interactor.AddTagUC;
import com.liueq.keyper.domain.interactor.GetAccountListUC;
import com.liueq.keyper.domain.interactor.SharedPUC;
import com.liueq.keyper.domain.interactor.GetStarListUC;
import com.liueq.keyper.domain.interactor.SearchAccountUC;
import com.liueq.keyper.domain.interactor.StarUC;
import com.liueq.keyper.utils.FileReader;

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
        SharedPUC sharedPUC = new SharedPUC((SharedPreferenceRepoImpl) spr);

        //Repository
        AccountRepo ar = new AccountRepoDBImpl(mainActivity, sharedPUC);
        StarRepo sr = new StarRepoDBImpl(mainActivity);
        TagRepo tr = new TagRepoDBImpl(mainActivity);

        //UseCase
        GetAccountListUC getAccountListUC = new GetAccountListUC(ar, sr, sharedPUC);
        SearchAccountUC searchAccountUC = new SearchAccountUC(ar, sr);
        StarUC starUC = new StarUC(sr);
        GetStarListUC getStarListUC = new GetStarListUC(ar, sr, sharedPUC);
        AddTagUC addTagUC = new AddTagUC(tr, sr);

        return new MainActivityPresenter(mainActivity, getAccountListUC, searchAccountUC, starUC, getStarListUC, addTagUC);
    }
}

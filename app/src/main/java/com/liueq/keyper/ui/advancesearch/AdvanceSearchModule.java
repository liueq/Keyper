package com.liueq.keyper.ui.advancesearch;

import com.liueq.keyper.base.ActivityScope;
import com.liueq.keyper.data.repository.AccountRepo;
import com.liueq.keyper.data.repository.AccountRepoDBImpl;
import com.liueq.keyper.data.repository.SharedPreferenceRepo;
import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;
import com.liueq.keyper.data.repository.StarRepo;
import com.liueq.keyper.data.repository.StarRepoDBImpl;
import com.liueq.keyper.domain.interactor.SearchAccountUC;
import com.liueq.keyper.domain.interactor.SharedPUC;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 7/3/2016.
 * Advance search module
 */
@Module
public class AdvanceSearchModule {

	AdvanceSearchActivity mActivity;

	public AdvanceSearchModule(AdvanceSearchActivity activity){
		mActivity = activity;
	}

	@Provides
	@ActivityScope
	AdvanceSearchActivity provideAdvanceSearchActivity(){
		return mActivity;
	}

	@Provides
	@ActivityScope
	AdvanceSearchPresenter provideAdvanceSearchPresenter(){
		SharedPreferenceRepo sharedRepo = new SharedPreferenceRepoImpl(mActivity);
		SharedPUC sharedPUC = new SharedPUC((SharedPreferenceRepoImpl) sharedRepo);

		AccountRepo ar = new AccountRepoDBImpl(mActivity, sharedPUC);
		StarRepo starRepo = new StarRepoDBImpl(mActivity);

		SearchAccountUC searchAccountUC = new SearchAccountUC(ar, starRepo);
		return new AdvanceSearchPresenter(mActivity, searchAccountUC);
	}
}

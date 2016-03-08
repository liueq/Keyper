package com.liueq.testdagger.ui.advancesearch;

import com.liueq.testdagger.base.ActivityScope;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.AccountRepoDBImpl;
import com.liueq.testdagger.data.repository.SharedPreferenceRepo;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.data.repository.StarRepo;
import com.liueq.testdagger.data.repository.StarRepoDBImpl;
import com.liueq.testdagger.domain.interactor.SearchAccountUC;
import com.liueq.testdagger.domain.interactor.SharedPUC;

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

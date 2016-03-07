package com.liueq.testdagger.ui.advancesearch;

import com.liueq.testdagger.base.ActivityScope;

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

		return new AdvanceSearchPresenter(mActivity);
	}
}

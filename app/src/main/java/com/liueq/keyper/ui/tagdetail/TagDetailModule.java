package com.liueq.keyper.ui.tagdetail;

import com.liueq.keyper.base.ActivityScope;
import com.liueq.keyper.data.repository.StarRepo;
import com.liueq.keyper.data.repository.StarRepoDBImpl;
import com.liueq.keyper.data.repository.TagRepo;
import com.liueq.keyper.data.repository.TagRepoDBImpl;
import com.liueq.keyper.domain.interactor.AddTagUC;
import com.liueq.keyper.domain.interactor.StarUC;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liueq on 29/2/2016.
 * TagDetailActivity Module
 */
@Module
public class TagDetailModule {

	private TagDetailActivity mActivity;

	public TagDetailModule(TagDetailActivity mActivity) {
		this.mActivity = mActivity;
	}

	@Provides
	@ActivityScope
	TagDetailActivity provideTagDetailActivity(){
		return mActivity;
	}

	@Provides
	@ActivityScope
	TagDetailActivityPresenter provideTagDetailActivityPresenter(){

		//Repository
		StarRepo sr = new StarRepoDBImpl(mActivity);
		TagRepo tr = new TagRepoDBImpl(mActivity);

		//UseCase
		StarUC starUC = new StarUC(sr);
		AddTagUC addTagUC = new AddTagUC(tr, sr);

		return new TagDetailActivityPresenter(mActivity, starUC, addTagUC);
	}
}
